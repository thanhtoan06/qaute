package vn.edu.hcmute.qaute.config.seed;

import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("demo")
public class DemoSeedRunner implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DemoSeedRunner.class);

    private final List<DemoSeeder> seeders;

    public DemoSeedRunner(List<DemoSeeder> seeders) {
        this.seeders = seeders;
    }

    @Override
    public void run(ApplicationArguments args) {
        seeders.stream()
                .sorted(Comparator.comparingInt(DemoSeeder::order))
                .forEach(seeder -> {
                    long started = System.nanoTime();
                    try {
                        seeder.seed();
                        LOG.info("Demo seeder {} hoàn tất sau {} ms",
                                seeder.getClass().getSimpleName(), elapsedMillis(started));
                    } catch (Exception exception) {
                        LOG.error("Demo seeder {} thất bại sau {} ms, tiếp tục",
                                seeder.getClass().getSimpleName(), elapsedMillis(started), exception);
                    }
                });
    }

    private long elapsedMillis(long started) {
        return (System.nanoTime() - started) / 1_000_000L;
    }
}
