object Versions {

    const val assessmentVersion = "0.4.4"

    const val kotlinxDateTime = "0.3.0"

    const val kotlin = "1.5.31"
    const val kotlinxSerializationJson = "1.3.0"
    const val kotlinCoroutines = "1.5.2-native-mt"

    const val ktor = "1.6.3"

    const val koin = "3.1.1"

    const val napier = "2.1.0"
}

object Deps {

    object Napier {
        val napier = "io.github.aakira:napier:${Versions.napier}"
    }

    object KotlinX {
        val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDateTime}"
        val serializationJson =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerializationJson}"
    }

    object AssessmentModel {
        val sdk =
            "org.sagebionetworks.assessmentmodel:assessmentModel:${Versions.assessmentVersion}"
    }

    object Ktor {
        val clientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
        val clientMock = "io.ktor:ktor-client-mock:${Versions.ktor}"

        val clientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
        val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"

        val clientAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
        val clientIos = "io.ktor:ktor-client-ios:${Versions.ktor}"
    }

    object Coroutines {
        val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
        val android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
        val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}"
    }

    object Koin {
        val core = "io.insert-koin:koin-core:${Versions.koin}"
        val test = "io.insert-koin:koin-test:${Versions.koin}"
        val android = "io.insert-koin:koin-android:${Versions.koin}"
        val androidWorkManager =  "io.insert-koin:koin-androidx-workmanager:${Versions.koin}"
    }
}
