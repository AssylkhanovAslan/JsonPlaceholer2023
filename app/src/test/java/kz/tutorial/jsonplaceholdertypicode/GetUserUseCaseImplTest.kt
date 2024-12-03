import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kz.tutorial.jsonplaceholdertypicode.data.use_case.GetUserUseCaseImpl
import kz.tutorial.jsonplaceholdertypicode.domain.entity.User
import kz.tutorial.jsonplaceholdertypicode.domain.repository.UserRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetUserUseCaseImplTest {

    @Mock
    lateinit var userRepository: UserRepository

    private lateinit var useCase: GetUserUseCaseImpl

    @Before
    fun setUp() {
        useCase = GetUserUseCaseImpl(userRepository)
    }

    @Test
    fun `invoke() should return user`() {
        val userId = 1
        val expectedUser = User("john@example.com", userId, "John Doe", "123456789",
            "john_doe", "Doe", "password")

        runBlocking {
            Mockito.`when`(userRepository.getUser(userId)).thenReturn(expectedUser)
            val result = useCase.invoke(userId)

            Mockito.verify(userRepository).getUser(userId)
            assertEquals(expectedUser, result)
        }
    }
}
