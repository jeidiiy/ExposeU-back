package wscrg.exposeuback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class ExposeUBackApplication

fun main(args: Array<String>) {
    runApplication<ExposeUBackApplication>(*args)
}
