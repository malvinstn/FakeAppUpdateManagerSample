# FakeAppUpdateManagerSample
A Repository to show how we can use FakeAppUpdateManager to create an integration test for in-app updates of an Android app.

`AppUpdateManager` and `Executor` is injected by Dagger and is replaced with `FakeAppUpdateManager` and a simple `Executor` when integration test is run.
