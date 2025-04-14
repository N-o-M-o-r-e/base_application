implement
==============
	dependencies {
	        implementation("com.github.N-o-M-o-r-e:base_application:Tag")
	}
===============
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
