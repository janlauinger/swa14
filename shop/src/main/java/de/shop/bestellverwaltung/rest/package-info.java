@XmlSchema(elementFormDefault = XmlNsForm.QUALIFIED,
           namespace = BESTELLVERWALTUNG_NS,
           xmlns = @XmlNs(prefix = "", namespaceURI = BESTELLVERWALTUNG_NS))
@XmlAccessorType(FIELD)

package de.shop.bestellverwaltung.rest;

import static de.shop.util.Constants.BESTELLVERWALTUNG_NS;
import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;