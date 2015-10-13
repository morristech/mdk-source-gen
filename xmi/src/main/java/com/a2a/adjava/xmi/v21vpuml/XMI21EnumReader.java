/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.a2a.adjava.xmi.v21vpuml;

import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.OWNEDLITERAL_NODE;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.OWNEDLITERAL_TYPE_ENUMERATIONLITERAL;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMITYPE_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIID2011_ATTR;

import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.uml.UmlDataType;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlEnum;

/**
 * Enumeration reader
 * 
 * @author lmichenaud
 * 
 */
public final class XMI21EnumReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI21EnumReader.class);

	/**
	 * Enum Reader
	 */
	private static XMI21EnumReader enumReader = new XMI21EnumReader();

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI21EnumReader getInstance() {
		return enumReader;
	}

	/**
	 * Constructor
	 */
	private XMI21EnumReader() {
		// private because singleton pattern
	}

	/**
	 * Read enumerations
	 * @param p_xModel model node
	 * @param p_oModelDictonnary model dictionary
	 * @throws Exception exception
	 */
	@SuppressWarnings("unchecked")
	public void readEnums(Element p_xModel, UmlDictionary p_oModelDictonnary) throws Exception {
		log.debug("read enumerations");
		
		// xmi example :
		// <ownedMember isAbstract="false" isActive="false" isLeaf="false" name="EnamClass" visibility="public" xmi:id="tqQC7AqAUDAUlwXt" xmi:type="uml:Enumeration">

		for (Element xEnum : (List<Element>) p_xModel
				.selectNodes(".//ownedMember[@*[ namespace-uri() and local-name()=\"type\"] = 'uml:Enumeration']")) {
			this.readEnum(xEnum, p_oModelDictonnary);
		}
	}

	/**
	 * Read enumeration
	 * @param p_xEnum enumeration node
	 * @param p_oModelDictonary model dictionary
	 * @return Uml enumeration
	 * @throws Exception exception
	 */
	@SuppressWarnings("unchecked")
	public UmlEnum readEnum(Element p_xEnum, UmlDictionary p_oModelDictonary) throws Exception {
		
		String sId = p_xEnum.attributeValue(XMIID2011_ATTR);
		String sName = p_xEnum.attributeValue(NAME_ATTR);


		// XMI example :
		// <ownedLiteral name="ONE" xmi:id="2NcC7AqAUDAUlwX6" xmi:type="uml:EnumerationLiteral">
		
		UmlDataType oUmlDataType = new UmlDataType(sName);
		UmlEnum r_oEnum = new UmlEnum(sName, oUmlDataType);
		for (Element xEnumValue : (List<Element>) p_xEnum.elements(OWNEDLITERAL_NODE)) {
			if (xEnumValue.attributeValue(XMITYPE_ATTR).equals(OWNEDLITERAL_TYPE_ENUMERATIONLITERAL)) {
				r_oEnum.addEnumValue(xEnumValue.attributeValue(NAME_ATTR));
			}
		}

		p_oModelDictonary.addDataType(sId, oUmlDataType);
		p_oModelDictonary.registerEnumeration(sId, r_oEnum);

		log.debug("  enumeration: {}, literals: {}, id: {}", new Object[] { r_oEnum.getName(),
				r_oEnum.getEnumValues().toString(), sId });

		return r_oEnum;
	}
}