pluginManagement {
	repositories {
		maven { url = uri("https://repo.spring.io/milestone") }
		maven { url = uri("https://repo.spring.io/snapshot") }
		gradlePluginPortal()
	}
}
rootProject.name = "tr-desktop-api"
include("entity_suffix_strategy")
include("entity_strategy")
include("entity_strategy")
