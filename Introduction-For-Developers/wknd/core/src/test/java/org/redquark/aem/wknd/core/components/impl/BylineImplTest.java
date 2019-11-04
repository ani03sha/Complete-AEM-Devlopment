package org.redquark.aem.wknd.core.components.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

import java.util.Collections;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Constants;
import org.redquark.aem.wknd.core.components.Byline;

import com.adobe.cq.wcm.core.components.models.Image;
import com.google.common.collect.ImmutableList;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * @author Anirudh Sharma
 *
 */
@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class BylineImplTest {

	// This exposes a mock AEM context that provides a number of AEM and Sling
	// abstractions.
	//
	// The ctx object will act as the entry point for most of our mock context.
	private final AemContext ctx = new AemContext();

	// Creates a mock object of type com.adobe.cq.wcm.core.components.models.Image.
	// Note that this is defined at the class level so that, as needed, @Test
	// methods can alter its behavior as needed.
	@Mock
	private Image image;

	// Creates a mock object of type ModelFactory. Note that this is a pure Mockito
	// mock and has no methods implemented on it. Note that this is defined at the
	// class level so that, as needed, @Test methods can alter its behavior as
	// needed.
	@Mock
	private ModelFactory modelFactory;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		// This method registers the Sling Model to be tested, into the mock AEM
		// Context, so it can be instantiated in the @Test methods.
		ctx.addModelsForClasses(BylineImpl.class);

		// This loads resource structures into the mock context, allowing the code to
		// interact with these resources as if they were provided by a real repository.
		// The resource definitions in the file BylineImplTest.json are loaded into the
		// mock JCR context under /content .
		ctx.load().json("/org/redquark/aem/wknd/core/components/impl/BylineImpl.json", "/content");

		// Registers mock behavior for when getModelFromWrappedRequest(..) is called on
		// the mock ModelFactory object. The result defined in thenReturn (..) is to
		// return the mock Image object.
		//
		// Note that this behavior is only invoked when:
		// the 1st parameter is equal to the ctx 's request object, the 2nd param is any
		// Resource object, and the 3rd param must be the Core Components Image class.
		//
		// We accept any Resource because throughout our tests we will be setting the
		// ctx.currentResource(...) to various mock resources defined in the
		// BylineImplTest.json . Note that we add the lenient() strictness because we
		// will later want to override this behavior of the ModelFactory.
		lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()), any(Resource.class), eq(Image.class)))
				.thenReturn(image);

		// Registers the mock ModelFactory object into the AemContext, with the highest
		// service ranking. This is required since the ModelFactory used in the
		// BylineImpl's init() is injected via the @OSGiService ModelFactory model
		// field. In order for the AemContext to inject our mock object, which handles
		// calls to getModelFromWrappedRequest(..) , we must register it as the highest
		// ranking Service of that type (ModelFactory).
		ctx.registerService(ModelFactory.class, modelFactory, Constants.SERVICE_RANKING, Integer.MAX_VALUE);
	}

	/**
	 * Test method for
	 * {@link org.redquark.aem.wknd.core.components.impl.BylineImpl#getName()}.
	 */
	@Test
	void testGetName() {

		// Setting the expected value
		final String expected = "Jane Doe";

		// Sets the context of the mock resource to evaluate the code against, so this
		// is set to /content/byline as that is where the mock byline content resource
		// is loaded.
		ctx.currentResource("/content/byline");

		// Instantiating the Byline Sling Model by adapting it from the mock Request
		// object.
		Byline byline = ctx.request().adaptTo(Byline.class);

		// Invokes the method we're testing, getName(), on the Byline Sling Model
		// object.
		String actual = byline.getName();

		// Asserts the expected value matches the value returned by the byline Sling
		// Model object. If these values are not equal, the test will fail.
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link org.redquark.aem.wknd.core.components.impl.BylineImpl#getOccupations()}.
	 */
	@Test
	void testGetOccupations() {

		// Expected result
		List<String> expected = new ImmutableList.Builder<String>().add("Blogger").add("Photographer").add("YouTuber")
				.build();

		// Sets the current resource to evaluate the context against to the mock
		// resource definition at /content/byline. This ensures the BylineImpl.java
		// executes in the context of our mock resource
		ctx.currentResource("/content/byline");

		// Instantiates the Byline Sling Model by adapting it from the mock Request
		// object.
		Byline byline = ctx.request().adaptTo(Byline.class);

		// Invokes the method we're testing, getOccupations() , on the Byline Sling
		// Model object.
		List<String> actual = byline.getOccupations();

		// Asserts expected list is the same as the actual list.
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link org.redquark.aem.wknd.core.components.impl.BylineImpl#isEmpty()}.
	 */
	@Test
	void testIsEmpty() {

		ctx.currentResource("/content/empty");
		Byline byline = ctx.request().adaptTo(Byline.class);

		assertTrue(byline.isEmpty());
	}

	@Test
	public void testIsEmptyWithoutName() {
		ctx.currentResource("/content/without-name");

		Byline byline = ctx.request().adaptTo(Byline.class);

		assertTrue(byline.isEmpty());
	}

	@Test
	public void testIsEmptyWithoutOccupations() {
		ctx.currentResource("/content/without-occupations");

		Byline byline = ctx.request().adaptTo(Byline.class);

		assertTrue(byline.isEmpty());
	}

	@Test
	public void testIsEmptyWithoutImage() {
		ctx.currentResource("/content/byline");

		lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()), any(Resource.class), eq(Image.class)))
				.thenReturn(null);

		Byline byline = ctx.request().adaptTo(Byline.class);

		assertTrue(byline.isEmpty());
	}

	@Test
	public void testIsEmptyWithoutImageSrc() {
		ctx.currentResource("/content/byline");

		lenient().when(image.getSrc()).thenReturn("");

		Byline byline = ctx.request().adaptTo(Byline.class);

		assertTrue(byline.isEmpty());
	}

	@Test
	public void testIsNotEmpty() {
		ctx.currentResource("/content/byline");
		lenient().when(image.getSrc()).thenReturn("/content/bio.png");

		Byline byline = ctx.request().adaptTo(Byline.class);

		assertFalse(byline.isEmpty());
	}

	@Test
	public void testGetOccupationsWithoutOccupations() {
		List<String> expected = Collections.emptyList();

		ctx.currentResource("/content/empty");
		Byline byline = ctx.request().adaptTo(Byline.class);

		List<String> actual = byline.getOccupations();

		assertEquals(expected, actual);
	}

	@Test
	public void testIsEmpty_WithEmptyArrayOfOccupations() {
		ctx.currentResource("/content/without-occupations-empty-array");

		Byline byline = ctx.request().adaptTo(Byline.class);

		assertTrue(byline.isEmpty());
	}
}
