package com.ebupt.portal.canyon.common.annotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义长度校验注解（支持中文）
 *
 * @author chy
 * @date 2019-03-08 09:56
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CheckLength.CheckLengthValidator.class)
public @interface CheckLength {

	String message() default "The string length beyond limit";

	/**
	 * 约束注解在验证时所属的组别，只有在指定组别下才进行校验
	 *
	 * @return
	 *              校验规则集合
	 */
	Class<?>[] groups() default {};

	/**
	 * 约束注解的有效负载
	 *
	 * @return
	 *              负载集合
	 */
	Class<? extends Payload>[] payload() default { };

	int min() default 0;

	int max() default Integer.MAX_VALUE;

	/**
	 * 定义List，可以使Bean的一个属性上添加多套规则
	 */
	@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
			ElementType.CONSTRUCTOR, ElementType.PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		CheckLength[] value();
	}

	class CheckLengthValidator implements ConstraintValidator<CheckLength, Object> {

		private int min;

		private int max;

		@Override
		public void initialize(CheckLength constraintAnnotation) {
			this.min = constraintAnnotation.min();
			this.max = constraintAnnotation.max();
		}

		@Override
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			if (value == null) {
				return true;
			}

			String str = value.toString();
			String chinese = "[\u4e00-\u9fa5]";

			int len = 0;
			for (int i = 0; i < str.length(); i++) {
				String tmp = str.substring(i, i + 1);
				// 中文字符串算两个
				if (tmp.matches(chinese)) {
					len += 2;
				} else {
					len += 1;
				}
			}

			return len >= min && len <= max;
		}
	}

}
