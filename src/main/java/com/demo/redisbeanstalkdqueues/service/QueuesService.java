package com.demo.redisbeanstalkdqueues.service;

import com.surftools.BeanstalkClient.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.demo.redisbeanstalkdqueues.service.QueueNameEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class QueuesService {
    
    private static final String TUBE = "testing_tube";

    private final Client beanstalkdClient;
    
    private final RedisTemplate<String, String> redisTemplateRdb;

    private final RedisTemplate<String, String> redisTemplateAof;

    public void produceMessages(QueueNameEnum queue) {
        String message = generateMessage();

        if (queue == BEANSTALKD) {
            beanstalkdClient.useTube(TUBE);
            long jobId = beanstalkdClient.put(1024, 0, 120, message.getBytes());
            logPublishedMessage(BEANSTALKD, jobId, message);
        } else if (queue == REDIS_RDB) {
            Long messageId = redisTemplateRdb.opsForList().rightPush(TUBE, message);
            logPublishedMessage(REDIS_RDB, messageId, message);
        } else if (queue == REDIS_AOF) {
            Long messageId = redisTemplateAof.opsForList().rightPush(TUBE, message);
            logPublishedMessage(REDIS_AOF, messageId, message);
        }
    }

    private static void logPublishedMessage(QueueNameEnum queueNameEnum, Long messageId, String message) {
        log.info("{}:: Produced job ID: [{}] with message: [{}]", queueNameEnum, messageId, message);
    }

    public void consumeMessages(QueueNameEnum queue) {
        if (queue == BEANSTALKD) {
            beanstalkdClient.watch(TUBE);
            beanstalkdClient.ignore("default");

            Optional.ofNullable(beanstalkdClient.reserve(1))
                    .ifPresent(job -> {
                        log.info("{}:: Consumed job ID: [{}]", BEANSTALKD, job.getJobId());
                        beanstalkdClient.delete(job.getJobId());
                    });
        } else if (queue == REDIS_RDB) {
            Optional.ofNullable(redisTemplateRdb.opsForList().leftPop(TUBE))
                    .ifPresent(message -> log.info("{}:: Consumed message: [{}]", REDIS_RDB, message));
        } else if (queue == REDIS_AOF) {
            Optional.ofNullable(redisTemplateAof.opsForList().leftPop(TUBE))
                    .ifPresent(message -> log.info("{}:: Consumed message: [{}]", REDIS_AOF, message));
        }
    }

    private String generateMessage() {
        return "message-%s".formatted(UUID.randomUUID());
    }
}
