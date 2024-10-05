/*
 * Copyright (c) 2020-2024 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.api;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.trebol.api.models.AppError;
import org.trebol.common.exceptions.BadInputException;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Catches some known exceptions, commonly declared at the controller level.<br/>
 * Then it sends custom responses to consumers of the REST API.<br/>
 *
 * The following exceptions are supported and mapped to certain status codes:
 * <ul>
 * <li>{@link jakarta.persistence.EntityNotFoundException} -> 404 NOT FOUND</li>
 * <li>{@link jakarta.persistence.EntityExistsException} -> 400 BAD REQUEST</li>
 * <li>{@link org.trebol.common.exceptions.BadInputException} -> 400 REQUEST</li>
 * <li>{@link org.springframework.web.bind.MethodArgumentNotValidException} -> 400 BAD REQUEST</li>
 * </ul>
 *
 * @see org.springframework.http.HttpStatus
 */
@RestControllerAdvice
public class ExceptionsControllerAdvice {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public AppError handleException(EntityNotFoundException ex) {
        return AppError.builder()
            .code("NOTFOUND_01")
            .message("The requested resource could not be found")
            .canRetry(false)
            .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(EntityExistsException.class)
    public AppError handleException(EntityExistsException ex) {
        return AppError.builder()
            .code("EXISTS_01")
            .message("There is one such resource with the same identifying properties that already exists")
            .canRetry(false)
            .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadInputException.class)
    public AppError handleException(BadInputException ex) {
        return AppError.builder()
            .code("REJECTED_01")
            .message("The request body did not meet the required criteria")
            .canRetry(true)
            .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AppError handleException(MethodArgumentNotValidException ex) {
        StringBuilder stringBuilder = new StringBuilder();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        int errorCollectionSize = allErrors.size();
        for (int i = 0; i < errorCollectionSize; i++) {
            ObjectError error = allErrors.get(i);
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            stringBuilder.append(String.format("%s: %s", fieldName, errorMessage));
            if (errorCollectionSize > i+1) {
                stringBuilder.append(String.format("%n"));
            }
        }
        return AppError.builder()
            .code("REJECTED_02")
            .message("The request body did not meet the basic required criteria for acceptance")
            .detailMessage(stringBuilder.toString())
            .build();
    }
}
