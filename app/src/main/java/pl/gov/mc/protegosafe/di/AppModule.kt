package pl.gov.mc.protegosafe.di

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.gov.mc.protegosafe.AppRepositoryImpl
import pl.gov.mc.protegosafe.domain.PushNotifier
import pl.gov.mc.protegosafe.domain.executor.PostExecutionThread
import pl.gov.mc.protegosafe.domain.repository.AppRepository
import pl.gov.mc.protegosafe.domain.usecase.ChangeServiceStatusUseCase
import pl.gov.mc.protegosafe.domain.usecase.PrepareMigrationIfRequiredUseCase
import pl.gov.mc.protegosafe.domain.usecase.CheckDeviceRootedUseCase
import pl.gov.mc.protegosafe.domain.usecase.ClearExposureNotificationDataUseCase
import pl.gov.mc.protegosafe.domain.usecase.CloseAppUseCase
import pl.gov.mc.protegosafe.domain.usecase.ComposeAppLifecycleStateBrideDataUseCase
import pl.gov.mc.protegosafe.domain.usecase.GetAnalyzeResultUseCase
import pl.gov.mc.protegosafe.domain.usecase.GetAppVersionNameUseCase
import pl.gov.mc.protegosafe.domain.usecase.GetExposureInformationUseCase
import pl.gov.mc.protegosafe.domain.usecase.GetFontScaleUseCase
import pl.gov.mc.protegosafe.domain.usecase.GetLocaleUseCase
import pl.gov.mc.protegosafe.domain.usecase.GetMigrationUrlUseCase
import pl.gov.mc.protegosafe.domain.usecase.GetNotificationDataAndClearUseCase
import pl.gov.mc.protegosafe.domain.usecase.GetSafetyNetAttestationTokenUseCase
import pl.gov.mc.protegosafe.domain.usecase.GetServicesStatusUseCase
import pl.gov.mc.protegosafe.domain.usecase.GetSystemLanguageUseCase
import pl.gov.mc.protegosafe.domain.usecase.OnGetBridgeDataUseCase
import pl.gov.mc.protegosafe.domain.usecase.OnPushNotificationUseCase
import pl.gov.mc.protegosafe.domain.usecase.OnSetBridgeDataUseCase
import pl.gov.mc.protegosafe.domain.usecase.ProcessPendingActivityResultUseCase
import pl.gov.mc.protegosafe.domain.usecase.ProvideDiagnosisKeysUseCase
import pl.gov.mc.protegosafe.domain.usecase.SaveMatchedTokenUseCase
import pl.gov.mc.protegosafe.domain.usecase.SaveNotificationDataUseCase
import pl.gov.mc.protegosafe.domain.usecase.SaveTriageCompletedUseCase
import pl.gov.mc.protegosafe.domain.usecase.SetAppLanguageUseCase
import pl.gov.mc.protegosafe.domain.usecase.StartExposureNotificationUseCase
import pl.gov.mc.protegosafe.domain.usecase.StopExposureNotificationUseCase
import pl.gov.mc.protegosafe.domain.usecase.StorePendingActivityResultUseCase
import pl.gov.mc.protegosafe.domain.usecase.UploadTemporaryExposureKeysUseCase
import pl.gov.mc.protegosafe.domain.usecase.UploadTemporaryExposureKeysWithCachedPayloadUseCase
import pl.gov.mc.protegosafe.ui.MainViewModel
import pl.gov.mc.protegosafe.ui.common.PushNotifierImpl
import pl.gov.mc.protegosafe.ui.home.HomeViewModel
import pl.gov.mc.protegosafe.ui.home.WebUrlProvider

val appModule = module {
    factory<PushNotifier> { PushNotifierImpl(get()) }
    factory { WebUrlProvider(get()) }
    factory<PostExecutionThread> { pl.gov.mc.protegosafe.executor.PostExecutionThread() }
    factory { Realm.getDefaultInstance() }
    single<AppUpdateManager> { AppUpdateManagerFactory.create(androidContext()) }
    single<AppRepository> { AppRepositoryImpl(get(), androidContext()) }
}

val useCaseModule = module {
    factory { OnGetBridgeDataUseCase(get(), get(), get(), get(), get(), get(), get()) }
    factory { OnSetBridgeDataUseCase(get(), get(), get(), get(), get(), get(), get()) }
    factory { OnPushNotificationUseCase(get(), get()) }
    factory { SaveNotificationDataUseCase(get()) }
    factory { GetNotificationDataAndClearUseCase(get()) }
    factory { StartExposureNotificationUseCase(get(), get(), get()) }
    factory { StopExposureNotificationUseCase(get(), get(), get()) }
    factory { GetServicesStatusUseCase(get()) }
    factory { ChangeServiceStatusUseCase(get(), get(), get()) }
    factory { ClearExposureNotificationDataUseCase(get(), get(), get()) }
    factory { ProvideDiagnosisKeysUseCase(get(), get(), get()) }
    factory { GetSafetyNetAttestationTokenUseCase(get(), get()) }
    factory {
        UploadTemporaryExposureKeysUseCase(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    factory { UploadTemporaryExposureKeysWithCachedPayloadUseCase(get(), get(), get()) }
    factory { SaveTriageCompletedUseCase(get(), get()) }
    factory { ComposeAppLifecycleStateBrideDataUseCase(get()) }
    factory { SaveMatchedTokenUseCase(get(), get()) }
    factory { StorePendingActivityResultUseCase(get(), get()) }
    factory { ProcessPendingActivityResultUseCase(get(), get()) }
    factory { GetExposureInformationUseCase(get(), get()) }
    factory { GetAnalyzeResultUseCase(get(), get(), get(), get()) }
    factory { CheckDeviceRootedUseCase(get(), get(), get(), get()) }
    factory { PrepareMigrationIfRequiredUseCase(get(), get()) }
    factory { GetMigrationUrlUseCase(get()) }
    factory { GetAppVersionNameUseCase(get(), get(), get()) }
    factory { GetSystemLanguageUseCase(get(), get(), get()) }
    factory { SetAppLanguageUseCase(get(), get(), get()) }
    factory { GetLocaleUseCase(get()) }
    factory { GetFontScaleUseCase(get(), get(), get()) }
    factory { CloseAppUseCase(get(), get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel {
        HomeViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
}
