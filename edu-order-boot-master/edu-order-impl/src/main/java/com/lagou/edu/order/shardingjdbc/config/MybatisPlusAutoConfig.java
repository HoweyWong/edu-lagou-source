package com.lagou.edu.order.shardingjdbc.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import java.util.Iterator;
import java.util.List;
import javax.sql.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author: ma wei long
 * @date:   2020年7月28日 上午12:32:58
 */
@Configuration
@AutoConfigureAfter(MybatisPlusConfig.class)
@EnableConfigurationProperties({MybatisPlusProperties.class})
public class MybatisPlusAutoConfig {

  private static final Logger logger = LoggerFactory.getLogger(MybatisPlusAutoConfig.class);
  private final MybatisPlusProperties properties;
  private final Interceptor[] interceptors;
  private final ResourceLoader resourceLoader;
  private final DatabaseIdProvider databaseIdProvider;
  private final List<ConfigurationCustomizer> configurationCustomizers;
  private final ApplicationContext applicationContext;

  public MybatisPlusAutoConfig(MybatisPlusProperties properties,
      ObjectProvider<Interceptor[]> interceptorsProvider, ResourceLoader resourceLoader,
      ObjectProvider<DatabaseIdProvider> databaseIdProvider,
      ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
      ApplicationContext applicationContext) {
    this.properties = properties;
    this.interceptors = (Interceptor[]) interceptorsProvider.getIfAvailable();
    this.resourceLoader = resourceLoader;
    this.databaseIdProvider = (DatabaseIdProvider) databaseIdProvider.getIfAvailable();
    this.configurationCustomizers = (List) configurationCustomizersProvider.getIfAvailable();
    this.applicationContext = applicationContext;
  }

  public void afterPropertiesSet() {
    this.checkConfigFileExists();
  }

  private void checkConfigFileExists() {
    if (this.properties.isCheckConfigLocation() && StringUtils
        .hasText(this.properties.getConfigLocation())) {
      Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
      Assert.state(resource.exists(), "Cannot find config location: " + resource
          + " (please add config file or check your Mybatis configuration)");
    }

  }

  @Bean
  @ConditionalOnMissingBean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
    factory.setDataSource(dataSource);
    factory.setVfs(SpringBootVFS.class);
    if (StringUtils.hasText(this.properties.getConfigLocation())) {
      factory
          .setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
    }

    this.applyConfiguration(factory);
    if (this.properties.getConfigurationProperties() != null) {
      factory.setConfigurationProperties(this.properties.getConfigurationProperties());
    }

    if (!ObjectUtils.isEmpty(this.interceptors)) {
      factory.setPlugins(this.interceptors);
    }

    if (this.databaseIdProvider != null) {
      factory.setDatabaseIdProvider(this.databaseIdProvider);
    }

    if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
      factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
    }

    if (StringUtils.hasLength(this.properties.getTypeEnumsPackage())) {
      factory.setTypeEnumsPackage(this.properties.getTypeEnumsPackage());
    }

    if (this.properties.getTypeAliasesSuperType() != null) {
      factory.setTypeAliasesSuperType(this.properties.getTypeAliasesSuperType());
    }

    if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
      factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
    }

    if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
      factory.setMapperLocations(this.properties.resolveMapperLocations());
    }

    GlobalConfig globalConfig = this.properties.getGlobalConfig();
    if (this.applicationContext.getBeanNamesForType(MetaObjectHandler.class, false, false).length
        > 0) {
      MetaObjectHandler metaObjectHandler = (MetaObjectHandler) this.applicationContext
          .getBean(MetaObjectHandler.class);
      globalConfig.setMetaObjectHandler(metaObjectHandler);
    }

    if (this.applicationContext.getBeanNamesForType(IKeyGenerator.class, false, false).length > 0) {
      IKeyGenerator keyGenerator = (IKeyGenerator) this.applicationContext
          .getBean(IKeyGenerator.class);
      globalConfig.getDbConfig().setKeyGenerator(keyGenerator);
    }

    if (this.applicationContext.getBeanNamesForType(ISqlInjector.class, false, false).length > 0) {
      ISqlInjector iSqlInjector = (ISqlInjector) this.applicationContext
          .getBean(ISqlInjector.class);
      globalConfig.setSqlInjector(iSqlInjector);
    }

    factory.setGlobalConfig(globalConfig);
    return factory.getObject();
  }

  private void applyConfiguration(MybatisSqlSessionFactoryBean factory) {
    MybatisConfiguration configuration = this.properties.getConfiguration();
    if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
      configuration = new MybatisConfiguration();
    }

    if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
      Iterator var3 = this.configurationCustomizers.iterator();

      while (var3.hasNext()) {
        ConfigurationCustomizer customizer = (ConfigurationCustomizer) var3.next();
        customizer.customize(configuration);
      }
    }

    factory.setConfiguration(configuration);
  }

  @Bean
  @ConditionalOnMissingBean
  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    ExecutorType executorType = this.properties.getExecutorType();
    return executorType != null ? new SqlSessionTemplate(sqlSessionFactory, executorType)
        : new SqlSessionTemplate(sqlSessionFactory);
  }

  @Configuration
  @Import({MybatisPlusAutoConfig.AutoConfiguredMapperScannerRegistrar.class})
  @ConditionalOnMissingBean({MapperFactoryBean.class})
  public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {

    public MapperScannerRegistrarNotFoundConfiguration() {
    }

    public void afterPropertiesSet() {
      MybatisPlusAutoConfig.logger.debug("No {} found.", MapperFactoryBean.class.getName());
    }
  }

  public static class AutoConfiguredMapperScannerRegistrar implements BeanFactoryAware,
      ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private BeanFactory beanFactory;
    private ResourceLoader resourceLoader;

    public AutoConfiguredMapperScannerRegistrar() {
    }

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
        BeanDefinitionRegistry registry) {
      if (!AutoConfigurationPackages.has(this.beanFactory)) {
        MybatisPlusAutoConfig.logger.debug(
            "Could not determine auto-configuration package, automatic mapper scanning disabled.");
      } else {
        MybatisPlusAutoConfig.logger.debug("Searching for mappers annotated with @Mapper");
        List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
        if (MybatisPlusAutoConfig.logger.isDebugEnabled()) {
          packages.forEach((pkg) -> {
            MybatisPlusAutoConfig.logger.debug("Using auto-configuration base package '{}'", pkg);
          });
        }

        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        if (this.resourceLoader != null) {
          scanner.setResourceLoader(this.resourceLoader);
        }

        scanner.setAnnotationClass(Mapper.class);
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(packages));
      }
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
      this.beanFactory = beanFactory;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
      this.resourceLoader = resourceLoader;
    }
  }

}
