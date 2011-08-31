package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginResponseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * PluginLoader�C���^�t�F�[�X�����������̃f�t�H���g�̃v���O�C�����[�_�D
 * <p>
 * ���̃N���X�̃C���X�^���X���쐬������CloadPlugin�C�܂���loadPlugins���\�b�h�Q��p���āC
 * �C�ӂ̃f�B���N�g���ȉ��̃v���O�C�������[�h���邱�Ƃ��ł���D
 * �P�Ƀf�t�H���g��plugins�f�B���N�g������S�Ẵv���O�C�������[�h����ꍇ��loadPlugins()���\�b�h���g���D
 * <p>
 * �܂��C�e�v���O�C���͌ʂ�XML�t�@�C����p���āC���p����N���X�Q�ɃN���X�p�X���w�肷�邱�Ƃ��ł��邪�C
 * ���̃N���X�̃��\�b�h�Q�𗘗p���邱�ƂŁC�f�t�H���g�ŃN���X�p�X��ʂ��t�@�C���̐ݒ�����邱�Ƃ��ł���D
 * �܂��CaddLibraryExtension���\�b�h�p���āC�f�t�H���g�Ń��C�u�����Ƃ݂Ȃ��t�@�C���̊g���q�Q���w�肷��D
 * ����ɂ���āC�e�v���O�C���̃f�B���N�g�������ɂ���t�@�C���ŁC�ݒ肵���g���q�����t�@�C���Q�ɂ�XML�Ŏw�肵�Ȃ��Ă�
 * �p�X��ʂ����Ƃ��ł���D���ɁCaddLibraryDirectoryName���\�b�h��p���āC
 * �e�v���O�C���̃��[�g�f�B���N�g���ȊO�̃f�B���N�g�������C�u�����t�@�C���̒u����Ƃ݂Ȃ��āC�w�肵���g���q�����t�@�C���Q��
 * �p�X��ʂ����Ƃ��ł���D
 * �Ⴆ�΁C
 * <pre>
 *    addLibraryExtensions("jar");
 *    addLibraryDirectoryName("lib");
 * </pre>
 * �Ƃ���ƁCXML�Ŏw�肵�Ȃ��Ă��e�v���O�C��������jar�t�@�C����lib�f�B���N�g���ȉ���jar�t�@�C���ɃN���X�p�X��
 * �ʂ����Ƃ��ł���D 
 * 
 * @author kou-tngt
 */
public class DefaultPluginLoader implements PluginLoader {

    /**
     * ���C�u�����t�@�C����u���f�t�H���g�f�B���N�g������ǉ����郁�\�b�h�D
     * @param libraryDir �ǉ�����f�t�H���g���C�u�����f�B���N�g����
     */
    public void addLibraryDirectoryName(final String libraryDir) {
        this.libraryDirectoryNames.add(libraryDir);
    }

    /**
     * ���C�u�����t�@�C���̊g���q��ǉ����郁�\�b�h�D
     * @param extension �ǉ����郉�C�u�����t�@�C���̊g���q���D
     */
    public void addLibraryExtension(String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        this.libraryExtensions.add(extension);
    }

    /**
     * �o�^����Ă���f�t�H���g���C�u�����f�B���N�g�����̃Z�b�g��Ԃ����\�b�h
     * @return �o�^����Ă���f�t�H���g���C�u�����f�B���N�g�����Z�b�g
     */
    public Set<String> getLibraryDirectoryNames() {
        return Collections.unmodifiableSet(this.libraryDirectoryNames);
    }

    /**
     * �o�^����Ă���f�t�H���g���C�u�����t�@�C���̊g���q��Ԃ����\�b�h
     * @return �o�^����Ă���f�t�H���g���C�u�����t�@�C���̊g���q
     */
    public Set<String> getLibraryExtensions() {
        return Collections.unmodifiableSet(this.libraryExtensions);
    }

    /**
     * �{�c�[���̃N���X�t�@�C���Q���u����Ă���ꏊ�̐e�f�B���N�g��������plugins�f�B���N�g����Ԃ����\�b�h
     * ������Ȃ����null��Ԃ��D
     * @return�@�{�c�[���̃N���X�t�@�C���Q���u����Ă���ꏊ�̐e�f�B���N�g��������plugins�f�B���N�g���D�����ł��Ȃ����null�D
     */
    public File getPluginsDirectory() {
        if (null != this.pluginsDirectory) {
            return this.pluginsDirectory;
        }

        try {
            return this.searchPluginsDirectory();
        } catch (final PluginLoadException e) {
            return null;
        }
    }

    /**
     * �v���O�C���\���ݒ���L�q����XML�t�@�C���̃t�@�C�������擾���郁�\�b�h�D
     * @return�@�v���O�C���\���ݒ���L�q����XML�t�@�C���̃t�@�C����
     */
    public String getPluginXmlFileName() {
        return this.pluginXmlFileName;
    }

    /**
     * �f�t�H���g��plugins�f�B���N�g������ApluginDirName�Ŏw�肳�ꂽ�f�B���N�g���������v���O�C�������[�h����
     * @param pluginDirName �v���O�C���f�B���N�g����
     * @return ���[�h�����v���O�C���N���X�̃C���X�^���X
     * @throws PluginLoadException �v���O�C���̃��[�h�Ɏ��s�����ꍇ�ɓ�������D�A���C���L�̗�O�̂����ꂩ�ɃP�[�X�ɊY���������͂����炪�D�悳���D
     * @throws IllegalPluginXmlFormatException ���[�h����v���O�C���̐ݒ�����L�q����XML�t�@�C���̌`�����������Ȃ��ꍇ�ɓ�������D
     * @throws IllegalPluginDirectoryStructureException ���[�h����v���O�C���̃f�B���N�g���\�����������Ȃ��ꍇ�ɓ�������D
     * @throws PluginClassLoadException �v���O�C���̃N���X���[�h�Ɏ��s�����ꍇ�ɓ�������D
     * @throws PluginResponseException ���[�h�����v���O�C������̉������Ȃ������ꍇ.
     */
    public AbstractPlugin loadPlugin(final String pluginDirName) throws PluginLoadException,
            IllegalPluginXmlFormatException, IllegalPluginDirectoryStructureException,
            PluginClassLoadException, PluginResponseException {
        return this.loadPlugin(this.searchPluginsDirectory(), pluginDirName);
    }

    /**
     * pluginsDir�Ŏw�肳�ꂽ�f�B���N�g���ȉ�����CpluginName�Ŏw�肳�ꂽ�f�B���N�g���������v���O�C�������[�h����
     * @param pluginsDir �v���O�C�����z�u�����f�B���N�g��
     * @param pluginDirName �v���O�C���̃��[�g�f�B���N�g��
     * @return ���[�h�����v���O�C���N���X�̃C���X�^���X
     * @throws PluginLoadException �v���O�C���̃��[�h�Ɏ��s�����ꍇ�ɓ�������D�A���C���L�̗�O�̂����ꂩ�ɃP�[�X�ɊY���������͂����炪�D�悳���D
     * @throws IllegalPluginXmlFormatException ���[�h����v���O�C���̐ݒ�����L�q����XML�t�@�C���̌`�����������Ȃ��ꍇ�ɓ�������D
     * @throws IllegalPluginDirectoryStructureException ���[�h����v���O�C���̃f�B���N�g���\�����������Ȃ��ꍇ�ɓ�������D
     * @throws PluginClassLoadException �v���O�C���̃N���X���[�h�Ɏ��s�����ꍇ�ɓ�������D
     * @throws PluginResponseException ���[�h�����v���O�C������̉������Ȃ������ꍇ.
     * @throws NullPointerException pluginsDir��null�̏ꍇ
     * @throws IllegalArgumentException pluginsDir�����݂��Ȃ��ꍇ�C�f�B���N�g���ł͂Ȃ��ꍇ
     */
    public AbstractPlugin loadPlugin(final File pluginsDir, final String pluginDirName)
            throws PluginLoadException, IllegalPluginXmlFormatException,
            IllegalPluginDirectoryStructureException, PluginClassLoadException,
            PluginResponseException {
        if (null == pluginsDir || null == pluginDirName) {
            throw new NullPointerException();
        }

        if (!pluginsDir.exists()) {
            throw new IllegalArgumentException(pluginsDir.getAbsolutePath() + " is not found.");
        }

        if (!pluginsDir.isDirectory()) {
            throw new IllegalArgumentException(pluginsDir.getAbsolutePath() + " is not directory.");
        }

        return this.loadPlugin(new File(pluginsDir, pluginDirName));
    }

    /**
     * �v���O�C�����̂̃f�B���N�g���𒼐�pluginRootDir�Ŏw�肵�ă��[�h���郁�\�b�h�D
     * @param pluginRootDir �v���O�C���̃��[�g�f�B���N�g��
     * @return ���[�h�����v���O�C���N���X�̃C���X�^���X
     * @throws PluginLoadException �v���O�C���̃��[�h�Ɏ��s�����ꍇ�ɓ�������D�A���C���L�̗�O�̂����ꂩ�ɃP�[�X�ɊY���������͂����炪�D�悳���D
     * @throws IllegalPluginXmlFormatException ���[�h����v���O�C���̐ݒ�����L�q����XML�t�@�C���̌`�����������Ȃ��ꍇ�ɓ�������D
     * @throws IllegalPluginDirectoryStructureException ���[�h����v���O�C���̃f�B���N�g���\�����������Ȃ��ꍇ�ɓ�������D
     * @throws PluginClassLoadException �v���O�C���̃N���X���[�h�Ɏ��s�����ꍇ�ɓ�������D
     * @throws PluginResponseException ���[�h�����v���O�C������̉������Ȃ������ꍇ.
     * @throws NullPointerException pluginRootDir��null�̏ꍇ
     * @throws IllegalArgumentException pluginRootDir�����݂��Ȃ��ꍇ�C�f�B���N�g���ł͂Ȃ��ꍇ
     */
    public AbstractPlugin loadPlugin(final File pluginRootDir) throws PluginLoadException,
            IllegalPluginXmlFormatException, IllegalPluginDirectoryStructureException,
            PluginClassLoadException, PluginResponseException {

        //�A�N�Z�X�������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == pluginRootDir) {
            throw new NullPointerException();
        }
        if (!pluginRootDir.exists()) {
            throw new IllegalArgumentException(pluginRootDir.getAbsolutePath() + " is not found.");
        }
        if (!pluginRootDir.isDirectory()) {
            throw new IllegalArgumentException(pluginRootDir.getAbsolutePath()
                    + " is not directory.");
        }
        //�����`�F�b�N�I��

        //�f�t�H���g�Ń��C�u�����ƔF�����ă��[�h����悤�Ɏw�肳��Ă�t�@�C�����擾
        final File[] defaultLibraryFiles = this.detectSpecifiedLibraryFiles(pluginRootDir);

        //xml���擾
        final File pluginXml = this.detectPluginXmlFile(pluginRootDir);
        if (null == pluginXml) {
            //xml���Ȃ�����
            throw new IllegalPluginDirectoryStructureException(this.pluginXmlFileName
                    + " is not found in " + pluginRootDir.getName() + ".");
        }

        //xml��������

        String pluginClassName = null;
        String[] classpathStrings = null;
        try {
            //xml�����
            final PluginXmlInterpreter interpreter = new DefaultPluginXmlInterpreter(pluginXml);
            //�v���O�C���N���X���ƃN���X�p�X�Q���擾
            pluginClassName = interpreter.getPluginClassName();
            classpathStrings = interpreter.getClassPathAttributeNames();
        } catch (final FileNotFoundException e) {//���肦�Ȃ�
            throw new IllegalPluginDirectoryStructureException(this.pluginXmlFileName
                    + " is not found in " + pluginRootDir.getName() + ".", e);
        } catch (final IOException e) {
            throw new PluginLoadException("Failed to read " + pluginXml.getAbsolutePath() + ".", e);
        } catch (final IllegalPluginXmlFormatException e) {
            throw e;
        }

        if (null == pluginClassName || 0 == pluginClassName.length()) {
            //plugin�N���X���w�肳��Ȃ�����
            throw new IllegalPluginXmlFormatException("Plugin entry class is not specifed in "
                    + pluginXml.getAbsolutePath());
        }

        //plugin�N���X���w�肳�ꂽ

        //�N���X�p�X��ʂ��ꏊ��URL�����
        final Set<URL> libraryClassPathSet = new LinkedHashSet<URL>();
        for (final File defaultLibrary : defaultLibraryFiles) {
            try {
                libraryClassPathSet.add(defaultLibrary.toURL());
            } catch (final MalformedURLException e) {
                //�����I�Ƀ��[�h���郉�C�u�����Ƃ��āC�f�B���N�g����T���Č������t�@�C����URL�����Ȃ������D
                //�������̃P�[�X�͗L�蓾�Ȃ����C������N�����Ă���������D
            }
        }

        if (null != classpathStrings) {
            for (final String classpath : classpathStrings) {
                try {
                    libraryClassPathSet.add((new File(pluginRootDir, classpath)).toURL());
                } catch (final MalformedURLException e) {
                    //���C�u�����Ƃ���XML�Ŏw�肳�ꂽ�t�@�C����URL�����Ȃ������D����XML�̃p�X�w�肪��������
                    throw new IllegalPluginXmlFormatException("Failed to allocate classpath value "
                            + classpath + " specifed in " + pluginXml.getAbsolutePath());
                }
            }
        }

        final URL[] libraryClassPathArray = new URL[libraryClassPathSet.size()];
        libraryClassPathSet.toArray(libraryClassPathArray);

        try {
            //���̃v���O�C����p��URL�N���X���[�_���쐬
            final URLClassLoader loader = new URLClassLoader(libraryClassPathArray);
            //������g���ăv���O�C���N���X�����[�h���ăC���X�^���X��
            final Class<?> pluginClass = loader.loadClass(pluginClassName);
            final AbstractPlugin plugin = (AbstractPlugin) pluginClass.newInstance();

            assert (null != plugin) : "Illeagal state: Plugin class's instance is null.";

            //�v���O�C���f�B���N�g�����Z�b�g
            plugin.setPluginRootdir(pluginRootDir);
            
            //�v���O�C���f�B���N�g���ȉ��ւ̃A�N�Z�X�p�[�~�b�V�������Z�b�g
            try{
                String filePath = pluginRootDir.getAbsolutePath() + File.separator+ "-";
                plugin.addPermission(new FilePermission(filePath, "read"));
                plugin.addPermission(new FilePermission(filePath, "write"));
                plugin.addPermission(new FilePermission(filePath, "delete"));
            } catch (SecurityException e){
                //�p�[�~�b�V�����������Ȃ��������ǁA���Ȃ���������Ȃ��̂ő�����.
                assert (false) : "Illegal state: Plugin directory's access permission can not created.";
            }

            //�v���O�C�����̍\�z�����݂�
            if (!this.createPluginInfo(plugin)) {
                throw new PluginResponseException("Failed to create plugin information about "
                        + pluginClassName + ". Plugin's information methods must return within "
                        + PLUGIN_METHODS_RESPONSE_TIME + " milli seconds.");
            }

            //���[�h->�L���X�g->�C���X�^���X��->�f�B���N�g���̃Z�b�g->�v���O�C�����̍\�z���S�Đ��������̂ŕԂ�.
            return plugin;
        } catch (final SecurityException e) {
            throw new PluginClassLoadException("Failed to load " + pluginClassName + ".", e);
        } catch (final ClassNotFoundException e) {
            throw new PluginClassLoadException("Failed to load " + pluginClassName + ".", e);
        } catch (final InstantiationException e) {
            throw new PluginClassLoadException("Failed to instanciate " + pluginClassName + ".", e);
        } catch (final IllegalAccessException e) {
            throw new PluginClassLoadException("Failed to access to " + pluginClassName + ".", e);
        } catch (final IllegalStateException e) {
            throw new PluginLoadException("Failed to set plugin root direcotyr.", e);
        }
    }

    /**
     * �f�t�H���g��plugins�f�B���N�g������S�Ẵv���O�C�������[�h���郁�\�b�h
     * �ʂ̃v���O�C���̃��[�h���s�ɂ���Ĕ���������O�͕Ԃ��Ȃ��D
     * @return ���[�h�ł����e�v���O�C���̃v���O�C���N���X���i�[���郊�X�g
     * @throws PluginLoadException �f�t�H���g��plugins�f�B���N�g���̌��o�Ɏ��s�����ꍇ�D
     */
    public List<AbstractPlugin> loadPlugins() throws PluginLoadException {
        return this.loadPlugins(this.searchPluginsDirectory());
    }

    /**
     * �w�肵���f�B���N�g���ȉ��ɂ���S�Ẵv���O�C�������[�h���郁�\�b�h�D
     * �ʂ̃v���O�C���̃��[�h���s�ɂ���Ĕ���������O�͕Ԃ��Ȃ��D
     * @param pluginsDir �v���O�C�����z�u����Ă���f�B���N�g��
     * @return�@���[�h�ł����e�v���O�C���̃v���O�C���N���X���i�[���郊�X�g
     * @throws NullPointerException pluginsDir��null�̏ꍇ
     * @throws IllegalArgumentException pluginsDir�����݂��Ȃ��ꍇ�C�f�B���N�g���ł͂Ȃ��ꍇ
     */
    public List<AbstractPlugin> loadPlugins(final File pluginsDir) {
        if (null == pluginsDir) {
            throw new NullPointerException();
        }

        if (!pluginsDir.exists()) {
            throw new IllegalArgumentException(pluginsDir.getAbsolutePath() + " is not found.");
        }

        if (!pluginsDir.isDirectory()) {
            throw new IllegalArgumentException(pluginsDir.getAbsolutePath() + " is not directory.");
        }

        final List<AbstractPlugin> result = new ArrayList<AbstractPlugin>(100);
        final File[] pluginDirs = pluginsDir.listFiles();

        final MessagePrinter errorPrinter = new DefaultMessagePrinter(new MessageSource() {
            public String getMessageSourceName() {
                return "PluginLoader#loadPlugins(File)";
            }
        }, MESSAGE_TYPE.ERROR);

        for (final File pluginDir : pluginDirs) {
            if (pluginDir.isDirectory()) {
                try {
                    final AbstractPlugin plugin = this.loadPlugin(pluginDir);
                    result.add(plugin);
                } catch (final PluginLoadException e) {
                    errorPrinter.println("Failed to load plugin : " + pluginDir.getName());
                } catch (final PluginResponseException e) {
                    errorPrinter.println(e.getMessage());
                }
            }
        }

        return result;
    }

    /**
     * �f�t�H���g���C�u�����N���X�f�B���N�g�����폜���郁�\�b�h�D
     * @param libraryDirName �폜���郉�C�u�����N���X�f�B���N�g��
     */
    public void removeLibraryDirectoryName(final String libraryDirName) {
        this.libraryDirectoryNames.remove(libraryDirName);
    }

    /**
     * �f�t�H���g���C�u�����t�@�C���̊g���q���폜���郁�\�b�h�D
     * @param exntension �폜����f�t�H���g���C�u�����t�@�C���̊g���q
     */
    public void removeLibraryExtension(final String exntension) {
        this.libraryExtensions.remove(exntension);
    }

    /**
     * �v���O�C���\�������L�q����XML�t�@�C������u�������郁�\�b�h�D
     * �f�t�H���g��plugin.xml
     * @param xmlFileName �u��������t�@�C����
     */
    public void setPluginXmlFileName(final String xmlFileName) {
        this.pluginXmlFileName = xmlFileName;
    }

    /**
     * �f�t�H���g���C�u�����f�B���N�g�����ǂ����𔻒肷�郁�\�b�h�D
     * ���̃��\�b�h���I�[�o�[���C�h���邱�Ƃɂ���āC�ǂ̃f�B���N�g�������C�u�����u����ƌ��Ȃ����̔��f��
     * ���R�Ɋg���ł���D
     * @param dir ����Ώۂ̃f�B���N�g��
     * @return ���C�u�����u����ƌ��Ȃ��ꍇ��true
     */
    protected boolean isLibraryDirectory(final File dir) {
        if (null == dir || !dir.exists() || !dir.isDirectory()) {
            return false;
        }

        final String directoryName = dir.getName();
        for (final String libDir : this.libraryDirectoryNames) {
            if (directoryName.equals(libDir)) {
                return true;
            }
        }

        return false;
    }

    /**
     * �f�t�H���g���C�u�����t�@�C�����ǂ����𔻒肷�郁�\�b�h�D
     * ���̃��\�b�h���I�[�o�[���C�h���邱�Ƃɂ���āC�ǂ̃t�@�C�������C�u�����ƌ��Ȃ����̔��f��
     * ���R�Ɋg���ł���D
     * @param file ����Ώۃt�@�C��
     * @return ���C�u�����ƌ��Ȃ��ꍇ��true
     */
    protected boolean isLibraryFile(final File file) {
        if (null == file || !file.exists() || !file.isFile()) {
            return false;
        }

        final String fileName = file.getName();
        for (final String extension : this.libraryExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    /**
     * �f�t�H���g��plugins�f�B���N�g����T�����郁�\�b�h�D
     * @return �f�t�H���g��plugins�f�B���N�g��
     * @throws PluginLoadException plugins�f�B���N�g���̒T�����Z�L�����e�B��ł��Ȃ������ꍇ�C�ŏI�I�Ɍ�����Ȃ������ꍇ
     * @throws IllegalPluginDirectoryStructureException �T�����ʂ̃f�B���N�g�����s���ȏꏊ�ł������ꍇ
     */
    protected synchronized File searchPluginsDirectory() throws PluginLoadException,
            IllegalPluginDirectoryStructureException {
        if (null != this.pluginsDirectory) {
            return this.pluginsDirectory;
        }

        File result = null;

        CodeSource source = null;
        try {
            source = this.getClass().getProtectionDomain().getCodeSource();
        } catch (final SecurityException e) {
            throw new PluginLoadException("Could not search plugins directory.", e);
        }

        if (null != source) {
            URI sourceUri = null;
            final URL sourceUrl = source.getLocation();
            try {
                sourceUri = sourceUrl.toURI();
            } catch (final URISyntaxException e) {
                throw new IllegalPluginDirectoryStructureException(
                        "Could not allocate plugins directory " + sourceUrl, e);
            }

            if (null != sourceUri) {
                File sourceRootDir = new File(sourceUri).getParentFile();

                assert (sourceRootDir.exists()) : "Illeagal state: "
                        + sourceRootDir.getAbsolutePath() + " is not found.";
                assert (sourceRootDir.isDirectory()) : "Illeagal state: "
                        + sourceRootDir.getAbsolutePath() + " is not direcotry.";

                final File[] directoryEntries = sourceRootDir.listFiles();
                for (final File directoryEntry : directoryEntries) {
                    if (directoryEntry.isDirectory()
                            && directoryEntry.getName().equals(PLUGINS_DIRECTORY_NAME)) {
                        result = directoryEntry;
                        break;
                    }
                }

            }
        }

        if (null != result) {
            assert (result.exists()) : result + " is not found.";
            assert (result.isDirectory()) : result + " is not plugins directory.";

            this.pluginsDirectory = result;
            return result;
        }

        throw new PluginLoadException("Plugins directory is not found.");
    }

    /**
     * �v���O�C������ʃX���b�h�ō\�z����.
     * �w�莞�Ԉȓ��ɍ\�z�ł��Ȃ������ꍇ�͒��߂�.
     * @param plugin �����\�z����v���O�C��.
     * @return �v���O�C�������w�莞�Ԉȓ��ɍ\�z�ł�����true�C�ł��Ȃ�������false.
     */
    private boolean createPluginInfo(final AbstractPlugin plugin) {
        final Thread creationThread = new Thread() {
            @Override
            public void run() {
                plugin.getPluginInfo();
            }
        };

        creationThread.start();
        try {
            creationThread.join(PLUGIN_METHODS_RESPONSE_TIME);//�\�z�܂Ŏw�莞�ԑ҂�
        } catch (final InterruptedException e) {
            //���߂�
        }
        return plugin.isPluginInfoCreated();
    }

    /**
     * �w�肳�ꂽ�f�B���N�g������v���O�C���ݒ�XML�t�@�C����T�����\�b�h
     * @param pluginRootDir �T���f�B���N�g��
     * @return XML�t�@�C���D������Ȃ����null�D
     */
    private File detectPluginXmlFile(final File pluginRootDir) {
        final File[] directoryEntries = pluginRootDir.listFiles();

        for (final File directoryEntry : directoryEntries) {
            if (directoryEntry.isFile()) {
                if (directoryEntry.getName().equals(this.pluginXmlFileName)) {
                    return directoryEntry;
                }
            }
        }

        return null;
    }

    /**
     * �w�肳�ꂽ�f�B���N�g������ȉ�����C�f�t�H���g�̃��C�u�����t�@�C���Q���������郁�\�b�h
     * @param pluginRootDir �w�肳�ꂽ�f�B���N�g��
     * @return �����������C�u�����t�@�C���Q
     */
    private File[] detectSpecifiedLibraryFiles(final File pluginRootDir) {
        final File[] directoryEntries = pluginRootDir.listFiles();

        final List<File> libraryDirectries = new ArrayList<File>();
        final List<File> libraryFiles = new ArrayList<File>();

        for (final File directoryEntry : directoryEntries) {
            if (directoryEntry.isFile()) {
                if (this.isLibraryFile(directoryEntry)) {
                    libraryFiles.add(directoryEntry);
                }
            } else if (directoryEntry.isDirectory()) {
                if (this.isLibraryDirectory(directoryEntry)) {
                    libraryDirectries.add(directoryEntry);
                }
            } else {
                assert (false) : "Unknown directory entry.";
            }
        }

        for (final File libraryDirectory : libraryDirectries) {
            assert (libraryDirectory.exists() && libraryDirectory.isDirectory()) : "Illeagal state: local variable libraryDirectories has unexpected File.";
            final File[] libraryDirEntries = libraryDirectory.listFiles();
            for (final File libraryDirEntry : libraryDirEntries) {
                if (libraryDirEntry.isFile()) {
                    if (this.isLibraryFile(libraryDirEntry)) {
                        libraryFiles.add(libraryDirEntry);
                    }
                }
            }
        }

        final File[] result = new File[libraryFiles.size()];
        return libraryFiles.toArray(result);
    }

    /**
     * �f�t�H���g���C�u�����t�@�C���̊g���q��Set
     */
    private final Set<String> libraryExtensions = new LinkedHashSet<String>();

    /**
     * �f�t�H���g���C�u�����f�B���N�g��񖼑O��Set
     */
    private final Set<String> libraryDirectoryNames = new LinkedHashSet<String>();

    /**
     * �v���O�C���̐ݒ�����L�q����XML�t�@�C����
     */
    private String pluginXmlFileName = DEFAULT_PLUGIN_XML_NAME;

    /**
     * ���o�����f�t�H���gplugins�f�B���N�g��
     */
    private File pluginsDirectory = null;

    /**
     * �f�t�H���g�̃v���O�C���ݒ���XML�̃t�@�C�����D
     */
    private static final String DEFAULT_PLUGIN_XML_NAME = "plugin.xml";

    /**
     * �f�t�H���gplugins�f�B���N�g�����D
     */
    private static final String PLUGINS_DIRECTORY_NAME = "plugins";

    /**
     * �v���O�C�����̍\�z���ɑ҂ő厞��.
     */
    private static final int PLUGIN_METHODS_RESPONSE_TIME = 5000;
}
