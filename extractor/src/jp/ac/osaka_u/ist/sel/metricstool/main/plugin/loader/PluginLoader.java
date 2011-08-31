package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


import java.io.File;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginResponseException;


/**
 * 
 * 
 * ���̃C���^�t�F�[�X�̓v���O�C�������[�h���邽�߂̃��\�b�h�Q��񋟂���D
 * loadPlugin�C�܂���loadPlugins���\�b�h�Q��p���āC�C�ӂ̃f�B���N�g���ȉ��̃v���O�C�������[�h���邱�Ƃ��ł���D
 * �P�Ƀf�t�H���g��plugins�f�B���N�g������S�Ẵv���O�C�������[�h����ꍇ��loadPlugins()���\�b�h���g���D
 * 
 * @author kou-tngt
 *
 */
public interface PluginLoader {
    /**
     * �f�t�H���g��plugins�f�B���N�g������ApluginDirName�Ŏw�肳�ꂽ�f�B���N�g���������v���O�C�������[�h����
     * @param pluginDirName �v���O�C���f�B���N�g����
     * @return ���[�h�����v���O�C���N���X�̃C���X�^���X
     * @throws PluginLoadException �v���O�C���̃��[�h�Ɏ��s�����ꍇ�ɓ�������D�A���C���L�̗�O�̂����ꂩ�ɃP�[�X�ɊY���������͂����炪�D�悳���D
     * @throws IllegalPluginXmlFormatException ���[�h����v���O�C���̐ݒ�����L�q����XML�t�@�C���̌`�����������Ȃ��ꍇ�D
     * @throws IllegalPluginDirectoryStructureException ���[�h����v���O�C���̃f�B���N�g���\�����������Ȃ��ꍇ�D
     * @throws PluginClassLoadException �v���O�C���̃N���X���[�h�Ɏ��s�����ꍇ�D
     * @throws PluginResponseException ���[�h�����v���O�C������̉������Ȃ������ꍇ.
     */
    public AbstractPlugin loadPlugin(String pluginDirName) throws PluginLoadException,
            IllegalPluginXmlFormatException, IllegalPluginDirectoryStructureException,
            PluginClassLoadException, PluginResponseException;

    /**
     * pluginsDir�Ŏw�肳�ꂽ�f�B���N�g���ȉ�����CpluginName�Ŏw�肳�ꂽ�f�B���N�g���������v���O�C�������[�h����
     * @param pluginsDir �v���O�C�����z�u�����f�B���N�g��
     * @param pluginDirName �v���O�C���̃��[�g�f�B���N�g��
     * @return ���[�h�����v���O�C���N���X�̃C���X�^���X
     * @throws PluginLoadException �v���O�C���̃��[�h�Ɏ��s�����ꍇ�ɓ�������D�A���C���L�̗�O�̂����ꂩ�ɃP�[�X�ɊY���������͂����炪�D�悳���D
     * @throws IllegalPluginXmlFormatException ���[�h����v���O�C���̐ݒ�����L�q����XML�t�@�C���̌`�����������Ȃ��ꍇ�D
     * @throws IllegalPluginDirectoryStructureException ���[�h����v���O�C���̃f�B���N�g���\�����������Ȃ��ꍇ�D
     * @throws PluginClassLoadException �v���O�C���̃N���X���[�h�Ɏ��s�����ꍇ�D
     * @throws PluginResponseException ���[�h�����v���O�C������̉������Ȃ������ꍇ.
     * @throws NullPointerException pluginsDir��null�̏ꍇ
     * @throws IllegalArgumentException pluginsDir�����݂��Ȃ��ꍇ�C�f�B���N�g���ł͂Ȃ��ꍇ
     */
    public AbstractPlugin loadPlugin(File pluginsDir, String pluginDirName)
            throws PluginLoadException, IllegalPluginXmlFormatException,
            IllegalPluginDirectoryStructureException, PluginClassLoadException,
            PluginResponseException;

    /**
     * �v���O�C�����̂̃f�B���N�g���𒼐�pluginRootDir�Ŏw�肵�ă��[�h���郁�\�b�h�D
     * @param pluginRootDir �v���O�C���̃��[�g�f�B���N�g��
     * @return ���[�h�����v���O�C���N���X�̃C���X�^���X
     * @throws PluginLoadException �v���O�C���̃��[�h�Ɏ��s�����ꍇ�ɓ�������D�A���C���L�̗�O�̂����ꂩ�ɃP�[�X�ɊY���������͂����炪�D�悳���D
     * @throws IllegalPluginXmlFormatException ���[�h����v���O�C���̐ݒ�����L�q����XML�t�@�C���̌`�����������Ȃ��ꍇ�D
     * @throws IllegalPluginDirectoryStructureException ���[�h����v���O�C���̃f�B���N�g���\�����������Ȃ��ꍇ�D
     * @throws PluginClassLoadException �v���O�C���̃N���X���[�h�Ɏ��s�����ꍇ�D
     * @throws PluginResponseException ���[�h�����v���O�C������̉������Ȃ������ꍇ.
     * @throws NullPointerException pluginRootDir��null�̏ꍇ
     * @throws IllegalArgumentException pluginRootDir�����݂��Ȃ��ꍇ�C�f�B���N�g���ł͂Ȃ��ꍇ
     */
    public AbstractPlugin loadPlugin(File pluginRootDir) throws PluginLoadException,
            IllegalPluginXmlFormatException, IllegalPluginDirectoryStructureException,
            PluginClassLoadException, PluginResponseException;

    /**
     * �f�t�H���g��plugins�f�B���N�g������S�Ẵv���O�C�������[�h���郁�\�b�h
     * �ʂ̃v���O�C���̃��[�h���s�ɂ���Ĕ���������O�͕Ԃ��Ȃ��D
     * @return ���[�h�ł����e�v���O�C���̃v���O�C���N���X���i�[���郊�X�g
     * @throws PluginLoadException �f�t�H���g��plugins�f�B���N�g���̌��o�Ɏ��s�����ꍇ�D
     */
    public List<AbstractPlugin> loadPlugins() throws PluginLoadException;

    /**
     * �w�肵���f�B���N�g���ȉ��ɂ���S�Ẵv���O�C�������[�h���郁�\�b�h�D
     * �ʂ̃v���O�C���̃��[�h���s�ɂ���Ĕ���������O�͕Ԃ��Ȃ��D
     * @param pluginsDir �v���O�C�����z�u����Ă���f�B���N�g��
     * @return�@���[�h�ł����e�v���O�C���̃v���O�C���N���X���i�[���郊�X�g
     * @throws NullPointerException pluginsDir��null�̏ꍇ
     * @throws IllegalArgumentException pluginsDir�����݂��Ȃ��ꍇ�C�f�B���N�g���ł͂Ȃ��ꍇ
     */
    public List<AbstractPlugin> loadPlugins(File pluginsDir);
}
