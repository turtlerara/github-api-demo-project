package com.demo.app.exception;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@StandardException
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GitHubServiceException extends RuntimeException {}
