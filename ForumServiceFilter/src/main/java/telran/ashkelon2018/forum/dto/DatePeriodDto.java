package telran.ashkelon2018.forum.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
public class DatePeriodDto {
	LocalDate from;
	LocalDate to;
}
