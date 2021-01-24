package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.hibernate.exception.ConstraintViolationException;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.transaction.Transactional;

@Service
public class UserBusinessService {

  @Autowired
  private UserDao userDao;

  @Autowired
  private PasswordCryptographyProvider cryptographyProvider;

  public UserEntity getUserById(final String userUuid) throws UserNotFoundException {
    UserEntity userEntity = userDao.getUserById(userUuid);

    if(userEntity == null) {
      throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
    }

    return userEntity;
  }


  @Transactional
  public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

    if(userDao.isUsernameExists(userEntity.getUsername())) {
      throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
    }

    if(userDao.isEmailExists(userEntity.getEmail())) {
      throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
    }

    String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
    userEntity.setSalt(encryptedText[0]);
    userEntity.setPassword(encryptedText[1]);

    UserEntity signUpUser = userDao.createUser(userEntity);

    System.out.println("GET USER NAME >>>> " + signUpUser.getUsername());

    return signUpUser;
  }

  @Transactional
  public UserAuthEntity signin(final String username, final String password) throws AuthenticationFailedException {
    UserEntity userEntity = userDao.getUserByUsername(username);

    if(userEntity == null) {
      throw new AuthenticationFailedException("ATH-001", "This username does not exist");
    }

    final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());
    if(encryptedPassword.equals(userEntity.getPassword())) {
      JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
      UserAuthEntity userAuthEntity = new UserAuthEntity();

      final ZonedDateTime now = ZonedDateTime.now();
      final ZonedDateTime expiresAt = now.plusHours(8);

      userAuthEntity.setUuid(UUID.randomUUID().toString());
      userAuthEntity.setUserId(userEntity);
      userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
      userAuthEntity.setExpiresAt(expiresAt);
      userAuthEntity.setLoginAt(now);

      userDao.createAuth(userAuthEntity);

      return userAuthEntity;
    } else {
      throw new AuthenticationFailedException("ATH-002", "Password failed");
    }
  }
}