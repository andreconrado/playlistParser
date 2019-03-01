package com.playlist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author 62000465 2019-03-01
 *
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest( classes = ParserController.class )
@WebAppConfiguration
public class Tester
{
	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext	webApplicationContext;

	@Before
	public void setUp()
	{
		mockMvc = MockMvcBuilders.webAppContextSetup( webApplicationContext ).build();
	}

	@Test
	public void test()
	{
		try
		{
			MvcResult andReturn = mockMvc.perform( MockMvcRequestBuilders.get( "/username/AndreConrado/password/W27eDSze3d/output/playlist.m3u8" ) )
							.andReturn();
			System.out.println( andReturn.getResponse().getStatus() );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testSeries()
	{
		try
		{
			MvcResult andReturn =
							mockMvc.perform( MockMvcRequestBuilders.get( "/series/username/AndreConrado/password/W27eDSze3d/output/playlist.m3u8" ) )
							.andReturn();
			System.out.println( andReturn.getResponse().getStatus() );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
}
