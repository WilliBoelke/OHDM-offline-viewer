# Testing 

**Note: there is a lot not tested yet.  But I hope my changes on the codebase made writing new tests a lot easier**

## Unit Tests

**Tests in the `test` directory **
The unit test are implemented using JUnit 5 and Mockito.

As I tried to improve the apps testability I decided to use AndroidX dependencies and following the MVVM Pattern.
In the end that didn't worked that well for the "normal" unit tests, because apparently they cant handle `LiveData`

In other words not the whole app can be tested using just JUnit 5 unit tests. Instead I started implementing android instrumented tests.

## Instrumented Tests

**tests in the androidTest directory**

For the instrumented tests AndroidX dependencies **Kotlin** and **Espresso** are used, since Mockito doesn't work that well with Kotlin I decided to use **Mockk** instead. 

As mentioned earlier some classes can't be tested using JUnit without complication. namely all view models, Repositories which are using `LiveData` 
and all Views of course.

The Views are tested using Espresso together with the `FragmentFactory` (to inject dependencies).

Using the `FragmentFactory ` to inject dependencies to test a View :

* Add the Dependency through the `FragmentFactory` Constructor 
* Write a Constructor which takes the dependency you want to inject in Fragment you want to test,
* Pass the Dependency inside the FragmentFactory to the View
* In The test use `FragmentScenario` and pass a FragmentFactory with you mocked dependency 

Example : 

MainActivity

``` java
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	// ...
   		getSupportFragmentManager().setFragmentFactory(new FragmentFactory(new SftpClient(), new YourDependency42));
   		// ...
   	}
```

FragmentFactory 

```java
    /**
     * Public constructor 
     * @param client
     * @param yourDependency 
     */
    public FragmentFactory(Client client, YourDependency42 yourDependency)
    {
        this.client = client;
        this.yourDependency = yourDependency;
    }
    
    
       @Override
    public Fragment instantiate(ClassLoader classLoader, String className)
    {
      // ...
        if (className.equals(YourFragment.class.getName()))
        {
            Log.d(TAG, "Instantiate : YourFragment");
            return new FragmentRequestStatus(this.yourDependency);
        }
      // ...
    }
    
```

View

```java
    
    public FragmentHome()
    {
        // doesn't do anything special
        // Always have an Empty constructor in your Fragment !
    }
    
    public FragmentHome(YourDependency42 yourDependency)
    {
       // Your code here
    }
    
```

Test 

```kotlin
val mockYourDependency = ... // mock your dependency here
val fragmentFactory = FragmentFactory(new SftpClient, mockYourDependency)
val bundle = Bundle()
val scenario = launchFragmentInContainer<FragmentDownloadCenterAll>(themeResId = R.style.LightTheme, fragmentArgs = bundle, 		factory = fragmentFactory)
```



The view models and Repositories can be tested using instrumented tests, nevertheless the tests can be implemented like normal unit tests.

There are a few things to notice: since they are working with LiveData objects which needs to be observed there must be an observer, but one which will just observe a single time- when the test runs. There is a class `OneTimeObserve` inside the androidTest directory which does exactly that.

Also you need to use the `instantTaskExecutorRule`  to make it work.

Example :



InstandExecutorRule 

``` kotlin
 @get:Rule
 val instantTaskExecutorRule = InstantTaskExecutorRule()
```



A Test using the OneTimeObserver

```kotlin
classUnderTest.MethodWhichReturnsTheLiveData().observeOnce {
                Assert.assertEquals(5, it.size)
            }
            
// Call methods to change the LiveData if you need
            
// the observeOnceMethod isn't a method of LiveData. It needs to be implemented within the test class

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
      val observer = OneTimeObserver(handler = onChangeHandler)
      observe(observer, observer)
      }

```

