package ru.bgitu.core.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.bgitu.core.common.getOrNull
import ru.bgitu.core.data.model.AuthStatus
import ru.bgitu.core.data.model.SignInResult
import ru.bgitu.core.data.util.NetworkMonitor
import ru.bgitu.core.data.testdoubles.TestCompassDatabase
import ru.bgitu.core.data.testdoubles.TestCompassService
import ru.bgitu.core.data.testdoubles.TestCompassService.Companion.AVATAR_URL_FROM_EOS
import ru.bgitu.core.data.testdoubles.TestCompassService.Companion.EOS_USER_ID
import ru.bgitu.core.data.testdoubles.TestCompassService.Companion.FULL_NAME
import ru.bgitu.core.data.testdoubles.TestCompassService.Companion.GROUP_NAME
import ru.bgitu.core.data.testdoubles.TestCompassService.Companion.USER_ID
import ru.bgitu.core.data_test.AlwaysOnlineMonitor
import ru.bgitu.core.database.CompassDatabase
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.datastore_test.testSettingsDataStore
import ru.bgitu.core.model.Group
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.model.request.RegisterEosUserRequest
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DefaultCompassAuthenticatorTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private lateinit var subject: DefaultCompassAuthenticator
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var database: CompassDatabase
    private lateinit var serviceApi: CompassService
    private lateinit var settings: SettingsRepository
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        networkMonitor = AlwaysOnlineMonitor()
        database = TestCompassDatabase()
        serviceApi = TestCompassService()
        settings = SettingsRepository(
            datastore = tmpFolder.testSettingsDataStore(
                coroutineScope = testScope,
            ),
            json = json
        )

        subject = DefaultCompassAuthenticator(
            serviceApi = serviceApi,
            settings = settings,
            database = database,
            networkMonitor = networkMonitor,
            ioDispatcher = UnconfinedTestDispatcher(),
            deviceId = "id"
        )
    }

    @Test
    fun `auth validation is passed after guest registration`() {
        testScope.launch {
            val group = Group(
                id = 1,
                name = "ПрИ-101",
            )

            val signInResult = subject.registerGuest(group)

            assertIs<SignInResult.Success>(signInResult)

            assertEquals(
                expected = AuthStatus.SIGNED_AS_GUEST,
                actual = subject.validateAuthentication().first()
            )
        }
    }

    @Test
    fun `all user data is cleared after signOut`() {
        testScope.runTest {
            subject.signOut()

            assertTrue(settings.hasNoUserData())
        }
    }

    @Test
    fun `remote account data is matching local data`() {
        testScope.runTest {
            val signInResult = subject.authWithEos(
                login = "",
                password = ""
            )

            assertIs<SignInResult.Success>(signInResult)

            val response = serviceApi.registerEosUser(
                RegisterEosUserRequest(
                    userId = USER_ID,
                    eosUserId = EOS_USER_ID,
                    eosGroupName = GROUP_NAME,
                    fullName = FULL_NAME,
                    avatarUrl = AVATAR_URL_FROM_EOS
                )
            ).getOrNull()

            assertNotNull(response)

            val localData = settings.data.first()
            val localCredentials = settings.credentials.first()

            val allDataSavedSuccessfully = arrayOf(
                response.groupId == localData.groupId,
                response.userId == localData.userId,
                response.eosUserId == assertNotNull(localData.compassAccount).eosUserId,
                response.credentials == localCredentials
            ).all { it }

            assertTrue(allDataSavedSuccessfully)
        }
    }
}