package com.userservice.util;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Document(collection="database_sequences")
public class DatabaseSequence {
	private String id;
	private long seq;

}
