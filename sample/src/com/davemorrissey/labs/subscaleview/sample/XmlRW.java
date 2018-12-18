package com.davemorrissey.labs.subscaleview.sample;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Elbit on 8/10/2017.
 */

public class XmlRW {

    private static final String TAG = "XmlRW";

    private String sProjectName = "";
    private String sSeries = "";
    private String sXlsxPath = "";
    private String sXlsxName = "";
    private String sXlsxDir = "";
    private String mXml;
    private HashMap<String, String> DataV;

    XmlRW(String xml, HashMap<String, String> args){
        sProjectName = args.get("ProjectName");       //projectName
        sSeries      = args.get("Series");                 //series;
        sXlsxPath     = args.get("XlsxPath");               //xlsPath;
        sXlsxName     = args.get("XlsxName");
        sXlsxDir      = args.get("XlsxDir");
        mXml         = xml;
    }

    XmlRW(String xml){
        mXml = xml;
    }

    public  HashMap<String, String> readXML() {
        DataV = new HashMap<String, String>();
        Document dom = null;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the
            // XML file
            try {
                dom = db.parse(new InputSource(new StringReader(mXml)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Element doc = dom.getDocumentElement();

            sProjectName = getTextValue(sProjectName, doc, "ProjectName");
            if (sProjectName != null) {
                if (!sProjectName.isEmpty())
                    DataV.put("ProjectName",sProjectName);
            }
            sSeries = getTextValue(sSeries, doc, "Series");
            if (sSeries != null) {
                if (!sSeries.isEmpty())
                    DataV.put("Series", sSeries);
            }
            sXlsxPath = getTextValue(sXlsxPath, doc, "XlsxPath");
            if (sXlsxPath != null) {
                if (!sXlsxPath.isEmpty())
                    DataV.put("XlsxPath",sXlsxPath);
            }
            sXlsxName = getTextValue(sXlsxName, doc, "XlsxName");
            if (sXlsxName != null) {
                if (!sXlsxName.isEmpty())
                    DataV.put("XlsxName",sXlsxName);
            }
            sXlsxDir = getTextValue(sXlsxDir, doc, "XlsxDir");
            if (sXlsxDir != null) {
                if (!sXlsxDir.isEmpty())
                    DataV.put("XlsDir",sXlsxDir);
            }
            return DataV;

        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveToXML() {
        Document dom;
        Element e = null;

        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();

            // create the root element
            Element rootEle = dom.createElement("Data");

            // create data elements and place them under root
            try{
                e = dom.createElement("ProjectName");
                e.appendChild(dom.createTextNode(sProjectName));
                rootEle.appendChild(e);

                e = dom.createElement("Series");
                e.appendChild(dom.createTextNode(sSeries));
                rootEle.appendChild(e);

                e = dom.createElement("XlsxPath");
                e.appendChild(dom.createTextNode(sXlsxPath));
                rootEle.appendChild(e);

                e = dom.createElement("XlsxName");
                e.appendChild(dom.createTextNode(sXlsxName));
                rootEle.appendChild(e);

                e = dom.createElement("XlsxDir");
                e.appendChild(dom.createTextNode(sXlsxDir));
                rootEle.appendChild(e);
            } catch (Exception err){
                Log.e(TAG, err.getStackTrace().toString());
            }



            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "data.dtd");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(mXml)));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private String getTextValue(String def, Element doc, String tag) {
        String value = def;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }


}
// USAGE:
//    private void WriteToXml() {
//        String xmlPath = Environment.getExternalStorageDirectory()+"/"+"Elbit Mark Target"+"/"+"Infrastructure"+"/Infrastructure.xml";
//        HashMap<String, String> Data = new HashMap<String, String>();
//        Data.put("ProjectName",mProjName);
//        Data.put("Series", mSeriesNumber);
//        Data.put("XlsPath",mFilePath);
//        Data.put("XlsDir",mFileDir);
//        Data.put("XlsName",mFileName);
//        XmlRW xml = new XmlRW(xmlPath, Data);
//        xml.saveToXML();
//    }

//    private void ReadFromXml() {
//        String xmlPath = Environment.getExternalStorageDirectory()+"/"+"Elbit Mark Target"+"/"+"Infrastructure"+"/Infrastructure.xml";
//        HashMap<String, String> Data = new HashMap<String, String>();
//        XmlRW xml = new XmlRW(xmlPath);
//        Data = xml.readXML();
//        String projName = Data.get("ProjectName");
//        if (projName.equals("")){return;}
//        mProjName = projName;
//        AutoCompleteTextView etProjName = (AutoCompleteTextView) findViewById(id.etProjName);
//        etProjName.setText(mProjName);
//        setTitleProjName(mProjName);
//
//        int currSeries = Integer.parseInt(Data.get("Series"))+1;
//        mSeriesNumber = String.valueOf(currSeries);
//        setSubTitleSer(mSeriesNumber);
//
//        mFilePath     = Data.get("XlsPath");
//        ImageButton imgbtnExcel = (ImageButton) findViewById(id.btnAddExcelFile);
//        imgbtnExcel.setImageResource(R.drawable.add_file_done);
//
//        mFileName     = Data.get("XlsName");
//        mFileDir      = Data.get("XlsDir");
//    }
