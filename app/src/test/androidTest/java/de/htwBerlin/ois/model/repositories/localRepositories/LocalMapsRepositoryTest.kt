package de.htwBerlin.ois.model.repositories.localRepositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.htwBerlin.ois.OneTimeObserver
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class LocalMapsRepositoryTest
{
        private lateinit var mockFile : File
        private lateinit var localFileList : Array<File>

        @get:Rule
        val instantTaskExecutorRule = InstantTaskExecutorRule()


        @Before
        fun setup() {

            val mockFileOne = mockk<File>()
            every {
                mockFileOne.getName()
            } returns "FileOne.map"
            every {
                mockFileOne.length()
            } returns 123

            val mockFileTwo = mockk<File>()
            every {
                mockFileTwo.getName()
            } returns "FileTwo.map"
            every {
                mockFileTwo.length()
            } returns 123

            val mockFileThree = mockk<File>()
            every {
                mockFileThree.getName()
            } returns "FileThree.map"
            every {
                mockFileThree.length()
            } returns 123

            val mockFileFour = mockk<File>()
            every {
                mockFileFour.getName()
            } returns "FileFour.map"
            every {
                mockFileFour.length()
            } returns 123

            val mockFileFive = mockk<File>()
            every {
                mockFileFive.getName()
            } returns "FileFive.map"
            every {
                mockFileFive.length()
            } returns 123

            localFileList = arrayOf(mockFileOne, mockFileTwo, mockFileThree, mockFileFour,  mockFileFive)

            mockFile = mockk<File>()
            every{
                mockFile.listFiles()
            } returns localFileList
        }

        @Test
        fun readFromDirectoryTest()
        {
            val localMapsRepository = LocalMapsRepository.getInstance()
            localMapsRepository.getLocalFiles(mockFile).observeOnce {
                Assert.assertEquals(5, it.size)
            }
        }

        fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
            val observer = OneTimeObserver(handler = onChangeHandler)
            observe(observer, observer)
        }
}