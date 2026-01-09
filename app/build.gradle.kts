plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.hilt)
	alias(libs.plugins.ksp)
}

android {
	namespace = "pl.com.app.exchange"
	compileSdk {
		version = release(36)
	}

	defaultConfig {
		applicationId = "pl.com.app.exchange"
		minSdk = 28
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"
		buildConfigField(
			"String",
			"API_URL",
			"\"https://api.nbp.pl/api/exchangerates/\""
		)
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
}
dependencies {
	with(libs) {
		implementation(platform(androidx.compose.bom))
		implementation(platform(okhttp.bom))
		implementation(bundles.core)
		implementation(bundles.di)
		implementation(bundles.network)
		implementation(bundles.ui)
		implementation(bundles.debugging)
		testImplementation(bundles.testing)
		ksp(hilt.compiler)
	}
}