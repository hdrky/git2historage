package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * PluginXmlInterpreter�N���X�́C�R���X�g���N�^�ň����Ƃ��ė^����ꂽ�v���O�C����`XML�t�@�C������͂��C
 * ���̒��ŋL�q����Ă���v���O�C�������擾����public���\�b�h�Q��񋟂���D
 * <p>
 * �v���O�C����`XML�t�@�C���̋L�q�`�����ύX���ꂽ�ꍇ�́C���̃N���X���C�����邩�C
 * ���邢�͂��̃N���X�̃T�u�N���X��V���ɍ쐬���ĐV�`���ɑΉ����Ȃ���΂Ȃ�Ȃ��D
 * 
 * @author kou-tngt
 */
public class DefaultPluginXmlInterpreter implements PluginXmlInterpreter{

    /**
     * PluginXmlInterpreter�N���X�̗B��̃R���X�g���N�^�D
     * �����Ƃ��ė^����ꂽXML�t�@�C������͂��C�v���O�C���N���X�ƃN���X�p�X�w������擾����D
     * @param pluginXml ��͑ΏۂƂ���v���O�C����`XML�t�@�C��
     * @throws PluginLoadException pluginXml��XML�̍\����͂��ł��Ȃ������Ƃ��C�܂��́CXML�\��������Ă����ꍇ
     * @throws IOException pluginXml �t�@�C���̓ǂݍ��݂Ɏ��s�����ꍇ
     * @throws IllegalPluginXmlFormatException pluginXml�t�@�C�����v���O�C����`XML�t�@�C���̌`���Ɉᔽ���Ă���ꍇ
     * @throws NullPointerException pluginXml��null�̏ꍇ
     * @throws IlleagalArgumentException�@pluginXml�����݂��Ȃ��ꍇ�C�t�@�C���ł͂Ȃ��ꍇ�C�܂��͊g���q��xml�ł͂Ȃ��ꍇ
     * 
     */
    public DefaultPluginXmlInterpreter(final File pluginXml) throws PluginLoadException,
            FileNotFoundException, IOException, IllegalPluginXmlFormatException {
        if (null == pluginXml) {
            throw new NullPointerException();
        }
        if (!pluginXml.exists()) {
            throw new FileNotFoundException(pluginXml + " is not found.");
        }
        if (!pluginXml.isFile()) {
            throw new IllegalArgumentException(pluginXml + " is not file.");
        }
        if (!pluginXml.getName().endsWith(".xml")) {
            throw new IllegalArgumentException(pluginXml + " is not xml file.");
        }
        //�����`�F�b�N�����܂�

        //��ɃG���[�\���̂��߂ɁC�t�@�C���������Ă���
        this.pluginXml = pluginXml;

        try {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            this.document = builder.parse(pluginXml);
        } catch (final ParserConfigurationException e) {
            //DocumentBuilder�̍\�z�Ɏ��s�����ꍇ�i�ǂ��������ɔ������邩�͕s���j
            throw new PluginLoadException("Failed to interpret " + pluginXml.getAbsolutePath()
                    + ".", e);
        } catch (final SAXException e) {
            //XML�\�����Ԉ���Ă���Document�����Ȃ������ꍇ�j
            throw new IllegalPluginXmlFormatException("Syntax error : "
                    + pluginXml.getAbsolutePath(), e);
        }

        if (!this.document.getDocumentElement().getNodeName().equals(PLUGIN_TAG)) {
            //XML�t�@�C���̒��g��<plugin>����n�܂��Ă��Ȃ����`�������������j
            throw new IllegalPluginXmlFormatException("Syntax error : The root tag must be <"
                    + PLUGIN_TAG + ">. at " + pluginXml.getAbsolutePath());
        }

        //XML����͂��ď����擾
        this.interpretPluginXml();
    }

    /**
     * ��͑Ώۂ�xml�t�@�C�����ɋL�q����Ă���C�v���O�C���N���X����Ԃ����\�b�h
     * @return �v���O�C���N���X��\��������
     */
    public String getPluginClassName() {
        assert (null != this.pluginClassName && 0 != this.pluginClassName.length()) : "Illegal state: this.pluginClassName is not initialized.";
        return this.pluginClassName;
    }

    /**
     * ��͑Ώۂ�xml�t�@�C�����ɋL�q����Ă���C�t�@�C���ւ̃N���X�p�X�w��ꗗ��Ԃ����\�b�h
     * @return �t�@�C���ւ̃N���X�p�X�w���\��������̔z��
     */
    public String[] getClassPathFileNames() {
        assert (null != this.classPathsToFile) : "Illegal state: this.classPathsToFile is not initialized.";
        return this.classPathsToFile;
    }

    /**
     * ��͑Ώۂ�xml�t�@�C�����ɋL�q����Ă���C�f�B���N�g���ւ̃N���X�p�X�w��ꗗ��Ԃ����\�b�h
     * @return �f�B���N�g���ւ̃N���X�p�X�w���\��������̔z��
     */
    public String[] getClassPathDirectoryNames() {
        assert (null != this.classPathsToDirectory) : "Illegal state: this.classPathsToDirectory is not initialized.";
        return this.classPathsToDirectory;
    }

    /**
     * ��͑Ώۂ�xml�t�@�C�����ɋL�q����Ă���C�N���X�p�X�w��ꗗ��Ԃ����\�b�h
     * @return �N���X�p�X�w���\��������̔z��
     */
    public String[] getClassPathAttributeNames() {
        assert (null != this.classPathsToAttribute) : "Illegal state: this.classPathsToAttribute is not initialized.";
        return this.classPathsToAttribute;
    }

    /**
     * ��͑Ώۂ�XML�t�@�C����\��DOM�h�L�������g��Ԃ����\�b�h�D
     * �T�u�N���X������h�L�������g���Q�Ƃł���悤�ɂ��邽�߁C�A�N�Z�X���x����protected�Ƃ���D
     * @return ��͑Ώ�XML��\��DOM�h�L�������g
     * @see org.w3c.dom.Document
     */
    protected final Document getDocument() {
        return this.document;
    }

    /**
     * xml����͂��v���O�C���N���X�̏����擾���郁�\�b�h�D
     * ���̃��\�b�h�̓R���X�g���N�^����Ăяo����邽�߁C���̃N���X��public�C�܂���protected�ȃC���X�^���X���\�b�h���Ăяo���Ă͂Ȃ�Ȃ��D
     */
    private void interpretPluginClassName() {
        final String className = this.document.getDocumentElement().getAttribute(CLASS_ATTRIBUTE);
        assert (null != className && 0 != className.length()) : "Failed to read plugin class name of "
                + this.pluginXml.getAbsolutePath();
        this.pluginClassName = className;
    }

    /**
     * xml����͂��N���X�p�X�����擾���郁�\�b�h�D
     * ���̃��\�b�h�̓R���X�g���N�^����Ăяo����邽�߁C���̃N���X��public�C�܂���protected�ȃC���X�^���X���\�b�h���Ăяo���Ă͂Ȃ�Ȃ��D
     */
    private void interpretClassPath() {
        //�N���X�p�X�����ꎞ�I�Ɋi�[���Ă����Z�b�g
        final Set<String> classPathToFileSet = new LinkedHashSet<String>();
        final Set<String> classPathToDirectorySet = new LinkedHashSet<String>();

        //<plugin>�^�O�����̎q���B
        final NodeList rootChildren = this.document.getDocumentElement().getChildNodes();
        final int rootChildreNum = rootChildren.getLength();

        for (int i = 0; i < rootChildreNum; i++) {
            final Node rootChild = rootChildren.item(i);
            if (rootChild.getNodeName().equals(CLASSPATH_TAG)) {
                //<classpath>�^�O������

                //<classpath>�^�O�����̎q���B
                final NodeList classpathNodes = rootChild.getChildNodes();
                final int classPathNum = classpathNodes.getLength();

                for (int j = 0; j < classPathNum; j++) {
                    final Node classPathNode = classpathNodes.item(j);
                    final String classPathTagName = classPathNode.getNodeName();

                    if (classPathTagName.equals(FILE_TAG)) {
                        //<file>�^�O������
                        final String classPathToFile = this.getNodeAttribute(classPathNode,
                                PATH_ATTRIBUTE);
                        if (null != classPathToFile && 0 != classPathToFile.length()) {
                            //�o�^
                            classPathToFileSet.add(classPathToFile);
                        }
                    } else if (classPathTagName.equals(DIRECTORY_TAG)) {
                        //<dir>�^�O������
                        final String classPathToDirectory = this.getNodeAttribute(classPathNode,
                                PATH_ATTRIBUTE);
                        if (null != classPathToDirectory && 0 != classPathToDirectory.length()) {
                            //�o�^
                            classPathToDirectorySet.add(classPathToDirectory);
                        }
                    } else if (classPathTagName.equals(TEXT_TAG)) {
                        //�������Ȃ�
                    } else {
                        //<file>�ł�<dir>�ł��Ȃ��^�O��<classpath>�����ɂ�����
                        //���s��͖�������Ζ��Ȃ����ǈꉞ�A�T�[�V�����G���[�𓊂���
                        assert (false) : "Unknown tag is found under <classpath> tag: "
                                + classPathTagName + " in " + this.pluginXml.getAbsolutePath();
                    }
                }
            }
        }

        //<file>�Ŏw�肳�ꂽ�N���X�p�X���t�B�[���h�̔z��ɕۑ�
        final int fileNum = classPathToFileSet.size();
        this.classPathsToFile = new String[fileNum];
        classPathToFileSet.toArray(this.classPathsToFile);

        //<dir>�Ŏw�肳�ꂽ�N���X�p�X���t�B�[���h�̔z��ɕۑ�
        final int dirNum = classPathToDirectorySet.size();
        this.classPathsToDirectory = new String[dirNum];
        classPathToDirectorySet.toArray(this.classPathsToDirectory);

        //<file>��<dir>�Ŏw�肳�ꂽ�N���X�p�X���t�B�[���h�̔z��ɕۑ�
        this.classPathsToAttribute = new String[fileNum + dirNum];
        int i = 0;
        for (final String classPath : classPathToFileSet) {
            this.classPathsToAttribute[i++] = classPath;
        }
        for (final String classPath : classPathToDirectorySet) {
            this.classPathsToAttribute[i++] = classPath;
        }
    }

    /**
     * xml����͂������擾���郁�\�b�h�D
     * ���̃��\�b�h�̓R���X�g���N�^����Ăяo����邽�߁C���̃N���X��public�C�܂���protected�ȃC���X�^���X���\�b�h���Ăяo���Ă͂Ȃ�Ȃ��D
     */
    private void interpretPluginXml() {
        this.interpretPluginClassName();
        this.interpretClassPath();
    }

    /**
     * org.w3c.dom.Node����CattributeName�Ŏw�肵�������̒l���擾���郁�\�b�h�D
     * node���w�肳�ꂽ���O�̑����������Ȃ��ꍇ��null��Ԃ��D
     * @param node �����l���擾����Ώۃm�[�h
     * @param attributeName �擾���鑮����
     * @return �����l��\��������Dnode���w�肳�ꂽ���O�̑����������Ȃ��ꍇ��null�D
     * @see org.w3c.dom.Node
     */
    private String getNodeAttribute(final Node node, final String attributeName) {
        final NamedNodeMap map = node.getAttributes();
        final Node attribute = map.getNamedItem(attributeName);

        if (null != attribute) {
            return attribute.getNodeValue();
        } else {
            return null;
        }
    }

    /**
     * ��͑Ώ�XML�t�@�C����DOM�h�L�������g
     */
    private final Document document;

    /**
     * ��͑Ώ�XML�t�@�C��
     */
    private final File pluginXml;;

    /**
     * ��͑Ώۂ�XML�t�@�C�����w�肷��v���O�C���N���X��
     */
    private String pluginClassName;

    /**
     * ��͑Ώۂ�XML�t�@�C�����w�肷��C�t�@�C���ɑ΂���N���X�p�X�v���̔z��
     */
    private String[] classPathsToFile;

    /**
     * ��͑Ώۂ�XML�t�@�C�����w�肷��C�f�B���N�g���ɑ΂���N���X�p�X�v���̔z��
     */
    private String[] classPathsToDirectory;

    /**
     * ��͑Ώۂ�XML�t�@�C�����w�肷��C�S�ẴN���X�p�X�v���̔z��
     */
    private String[] classPathsToAttribute;

    /**
     * �v���O�C���ݒ���XML�t�@�C���̃��[�g�^�O��plugin��\��������萔
     */
    private static final String PLUGIN_TAG = "plugin";

    /**
     * �v���O�C���ݒ���XML�t�@�C���̃N���X�p�X�^�O��classpath��\��������萔
     */
    private static final String CLASSPATH_TAG = "classpath";

    /**
     * �v���O�C���ݒ���XML�t�@�C���́C�t�@�C���ɑ΂���N���X�p�X�w��̃^�O��file��\��������萔
     */
    private static final String FILE_TAG = "file";

    /**
     * XML�t�@�C���́C�e�L�X�g����\���^�O
     */
    private static final String TEXT_TAG = "#text";

    /**
     * �v���O�C���ݒ���XML�t�@�C���́C�f�B���N�g���ɑ΂���N���X�p�X�w��̃^�O��dir��\��������萔
     */
    private static final String DIRECTORY_TAG = "dir";

    /**
     * �v���O�C���ݒ���XML�t�@�C���́C�v���O�C���N���X����l�Ɏ���������\���萔������
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * �v���O�C���ݒ���XML�t�@�C���́C�N���X�p�X��l�Ɏ���������\���萔������
     */
    private static final String PATH_ATTRIBUTE = "path";
}
