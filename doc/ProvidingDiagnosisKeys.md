# Providing Diagnosis Keys

Each new downloaded Diagnosis Key file (please see [Downloading Diagnosis Keys](DownloadingDiagnosisKeys.md) section) is provided for exposure checking. The check is performed by [Exposure Notification API](https://www.google.com/covid19/exposurenotifications/) with Exposure Configuration options that tune the matching algorithm. In order to provide elastic architecture, the exposure configuration is obtained from Firebase RemoteConfig service.
Providing of Diagnosis Key files is done in a background Worker as this is done periodically and doesn't require the user to have application in the foreground.

Steps:
- New Diagnosis Key files that have never been analyzed are downloaded as described in [Downloading Diagnosis Keys](DownloadingDiagnosisKeys.md) section. Diagnosis Key files must be signed appropriately - the matching algorithm only runs on data that has been verified with the public key distributed by the device configuration mechanism. See more [here](https://static.googleusercontent.com/media/www.google.com/pt-BR//covid19/exposurenotifications/pdfs/Exposure-Key-File-Format-and-Verification.pdf).
- Exposure configuration options are obtained from Firebase RemoteConfig. The configuration allows to provide Health Authority recommendations regarding different aspects of exposure (like duration, attenuation or days since exposure). More about ExposureConfiguration can be found [here](https://static.googleusercontent.com/media/www.google.com/en//covid19/exposurenotifications/pdfs/Android-Exposure-Notification-API-documentation-v1.3.2.pdf).
  - Repository function: [RemoteConfigurationRepository.getExposureConfigurationItem()](../domain/src/main/java/pl/gov/mc/protegosafe/domain/repository/RemoteConfigurationRepository.kt)
  - Repository implementation: [RemoteConfigurationRepositoryImpl.getExposureConfigurationItem()](../data/src/main/java/pl/gov/mc/protegosafe/data/repository/RemoteConfigurationRepositoryImpl.kt)
- The new downloaded Diagnosis Key files and the Exposure Configuration are provided for [Exposure Notification API](https://www.google.com/covid19/exposurenotifications/)
  - Worker: [ProvideDiagnosisKeyWorker](../device/src/main/java/pl/gov/mc/protegosafe/scheduler/ProvideDiagnosisKeyWorker.kt)
  - UseCase: [ProvideDiagnosisKeysUseCase](../domain/src/main/java/pl/gov/mc/protegosafe/domain/usecase/ProvideDiagnosisKeysUseCase.kt)
  - Repository function: [ExposureNotificationRepository.provideDiagnosisKeys(files: List<File>, token: String, exposureConfigurationItem: ExposureConfigurationItem)](../domain/src/main/java/pl/gov/mc/protegosafe/domain/repository/ExposureNotificationRepository.kt)
  - Repository implementation: [ExposureNotificationRepositoryImpl.provideDiagnosisKeys(files: List<File>, token: String, exposureConfigurationItem: ExposureConfigurationItem)](../data/src/main/java/pl/gov/mc/protegosafe/data/repository/ExposureNotificationRepositoryImpl.kt)
- When the data has been successfully provided for [Exposure Notification API](https://www.google.com/covid19/exposurenotifications/) then:
  - Information about the latest analyzed Diagnosis Key file timestamp is stored locally on a device (encrypted Shared Preferences). This information is used to select the future Diagnosis Key files that have not been analyzed yet – only files with higher Diagnosis Key timestamp are selected
    - Repository function: [DiagnosisKeyRepository.setLatestProcessedDiagnosisKeyTimestamp(timestamp: Long)](../domain/src/main/java/pl/gov/mc/protegosafe/domain/repository/DiagnosisKeyRepository.kt)
    - Repository implementation: [DiagnosisKeyRepositoryImpl.setLatestProcessedDiagnosisKeyTimestamp(timestamp: Long)](../data/src/main/java/pl/gov/mc/protegosafe/data/repository/DiagnosisKeyRepositoryImpl.kt)
  - All sent to analysis Diagnosis Key files are deleted from device internal storage
    - Worker function: [ProvideDiagnosisKeyWorker.finalizeDiagnosisKeyProviding(diagnosisKeyFiles: List<File>)](../device/src/main/java/pl/gov/mc/protegosafe/scheduler/ProvideDiagnosisKeyWorker.kt)
  - Diagnosis keys analysis will be performed by [Exposure Notification API](https://www.google.com/covid19/exposurenotifications/), after which application will [receive](ReceivingExposuresInformation.md) a broadcast message in case any exposure is detected