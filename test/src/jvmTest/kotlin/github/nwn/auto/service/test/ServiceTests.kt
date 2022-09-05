package github.nwn.auto.service.test

import github.nwn.auto.service.AutoService
import org.junit.jupiter.api.Test
import java.util.ServiceLoader
import kotlin.test.assertContains

class ServiceTests {

    @Test
    fun `Service Registration Test`() {
        val implementations = ServiceLoader.load(TestService::class.java).map { it() }
        assertContains(implementations,TestServiceImplementation.OUTPUT)
        assertContains(implementations,TestServiceImplementationObject.OUTPUT)
    }

}

interface TestService {
    operator fun invoke(): String
}
@AutoService(TestService::class)
class TestServiceImplementation : TestService {
    companion object {
        const val OUTPUT = "TestServiceImplementation"
    }

    override fun invoke(): String {
        return OUTPUT
    }

}
@AutoService(TestService::class)
object TestServiceImplementationObject : TestService {

    const val OUTPUT = "TestServiceImplementationObject"
    override fun invoke(): String {
        return OUTPUT
    }

}