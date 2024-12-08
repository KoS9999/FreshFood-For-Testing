package utevn.ff.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
	@NotEmpty
	@Length(min = 8)
	private String newPassword;
	@NotEmpty
	@Length(min = 8)
	private String confirmPassword;
}
