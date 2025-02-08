package co.jht.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import static co.jht.constants.ApplicationConstants.TASK_CODE_PREFIX;

@Service
public class TaskCodeGenerator {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TaskCodeGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generateTaskCode() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('task_code_seq')", Long.class);
        return String.format("%s%05d", TASK_CODE_PREFIX, nextVal);
    }
}