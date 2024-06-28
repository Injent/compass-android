package ru.bgitu.core.data.model

import org.junit.Test
import ru.bgitu.core.database.entity.SubjectEntity
import ru.bgitu.core.network.model.NetworkSubject
import kotlin.test.assertEquals

class SubjectKtTest {

    @Test
    fun `network subject can be  mapped  to subject entity`() {
        val networkSubject = NetworkSubject(
            subjectId = 1,
            name = "Physics"
        )

        val entitySubject = networkSubject.asEntity()

        assertEquals(
            SubjectEntity(
                id = 1,
                name = "Physics"
            ),
            entitySubject
        )
    }
}