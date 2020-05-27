package pl.gov.mc.protegosafe.domain.model

interface OutgoingBridgeDataResultComposer {
    fun composeTemporaryExposureKeysUploadResult(state: TemporaryExposureKeysUploadState): String
    fun composeAppLifecycleStateResult(state: AppLifecycleState): String
    fun composeAnalyzeResult(exposure: ExposureItem): String
}
