//package com.example.MyPersonalContactManager.config;
//
//
//import com.example.MyPersonalContactManager.repository.ContactRepositoryAdapter;
//import com.example.MyPersonalContactManager.repository.JdbcContactRepositoryImp;
//import com.example.MyPersonalContactManager.repository.interfaces.JpaContactRepositoryInterface;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Bean
//    public JdbcContactRepositoryImp dbRepository(JdbcTemplate jdbcTemplate) {
//        return new JdbcContactRepositoryImp(jdbcTemplate);
//    }
//
////    @Bean
////    public ContactServiceImp dbService(JdbcContactRepositoryImp dbRepository) {
////        return new ContactServiceImp(dbRepository);
////    }
//
//    @Bean
//    @Primary
//    public ContactRepositoryAdapter adapter(JpaContactRepositoryInterface jpaRepository) {
//        return new ContactRepositoryAdapter(jpaRepository);
//    }
//}
