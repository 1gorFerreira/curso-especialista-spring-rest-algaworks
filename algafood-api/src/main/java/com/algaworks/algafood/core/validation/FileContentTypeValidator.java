package com.algaworks.algafood.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

	private String[] allowedFiles;
	
	@Override
	public void initialize(FileContentType constraintAnnotation) {
		this.allowedFiles = constraintAnnotation.allowed();
	}
	
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		for(String type : allowedFiles) {
			if(value == null || type.equals(value.getContentType())) {
				return true;
			}
		}
		return false;
	}
	
	/*private List<String> allowedContentTypes;
	 *
	 * @Override
	 * public void initialize(FileContentType constraint) {
	 *   this.allowedContentTypes = Arrays.asList(constraint.allowed());
	 *  }
	 *
	 * @Override
	 * public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
	 *	return multipartFile == null 
	 *			|| this.allowedContentTypes.contains(multipartFile.getContentType());
	 *  }
	 */
}