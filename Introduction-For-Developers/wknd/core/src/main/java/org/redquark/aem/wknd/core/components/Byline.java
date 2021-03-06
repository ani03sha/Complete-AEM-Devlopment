package org.redquark.aem.wknd.core.components;

import java.util.List;

/**
 * Represents the AEM Byline component for the WKND Sites
 * 
 * @author Anirudh Sharma
 *
 */
public interface Byline {

	/**
	 * @return a string to display as the name
	 */
	String getName();

	/**
	 * Occupations are to be sorted alphabetically in descending order
	 * 
	 * @return a list of occupations
	 */
	List<String> getOccupations();

	/**
	 * @return a boolean if the component has content to display
	 */
	boolean isEmpty();
}
