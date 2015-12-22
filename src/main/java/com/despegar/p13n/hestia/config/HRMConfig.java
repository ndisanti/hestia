package com.despegar.p13n.hestia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.despegar.framework.media.interceptor.impl.GZipMediaManagerInterceptor;
import com.despegar.framework.media.manager.impl.FileMediaManager;
import com.despegar.framework.serialization.impl.BinarySerializer;
import com.despegar.p13n.commons.zk.ZKFactory;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.external.hrm.HRMClient;
import com.despegar.p13n.hestia.external.hrm.HRMService;
import com.despegar.p13n.hestia.external.hrm.HRMServiceImpl;
import com.despegar.p13n.hestia.external.hrm.HRMSnapshot;
import com.google.common.base.Preconditions;

@Configuration
public class HRMConfig {

    @Value("${hrm.snapshot.path}")
    private String snapshotPath;

    @Value("${hrm.service.quartz.cronExpression:0 0 1 * * *}")
    private String snapshotReloadExpression;

    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    @Qualifier("hrmClientV3")
    private HRMClient client;

    @Autowired
    private GeoService geoService;

    @Autowired
    private ZKFactory zkRecipesFactory;

    @Bean
    public HRMService hrmService() {
        Preconditions.checkState(this.snapshotReloadExpression != null);
        HRMServiceImpl hrmServiceImpl = new HRMServiceImpl();
        hrmServiceImpl.setZkRecipesFactory(this.zkRecipesFactory);

        FileMediaManager<HRMSnapshot> fileMediaManager = this.hrmMediaManager();
        hrmServiceImpl.setMediaManager(fileMediaManager);

        hrmServiceImpl.setGeoService(this.geoService);
        hrmServiceImpl.setHrmClient(this.client);

        hrmServiceImpl.init();

        // -----------------------//
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.initialize();
        this.taskScheduler.schedule(() -> hrmServiceImpl.reload(), new CronTrigger(this.snapshotReloadExpression));
        return hrmServiceImpl;
    }

    private FileMediaManager<HRMSnapshot> hrmMediaManager() {
        FileMediaManager<HRMSnapshot> fileMediaManager = new FileMediaManager<HRMSnapshot>();
        fileMediaManager.setPath(this.snapshotPath);
        fileMediaManager.setPrefix("snapshot_");
        BinarySerializer<HRMSnapshot> serializer = new BinarySerializer<>();
        fileMediaManager.setSerializer(serializer);
        fileMediaManager.setMediaManagerInterceptor(new GZipMediaManagerInterceptor());
        return fileMediaManager;
    }
}
