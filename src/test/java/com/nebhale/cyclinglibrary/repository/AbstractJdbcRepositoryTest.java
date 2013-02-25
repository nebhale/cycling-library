
package com.nebhale.cyclinglibrary.repository;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(classes = { RepositoryConfiguration.class, TestRepositoryConfiguration.class })
public abstract class AbstractJdbcRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

}
