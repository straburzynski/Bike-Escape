package com.bikeescape.api.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoggedInTO {

	private Long id;
	private List<String> authorities;
	private String firstName;
	private String email;

}
