package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


/**
 * 
 * ���̗�O�́C�v���O�C�������[�h���邽�߂̃N���X���[�_�������ł��Ȃ��ꍇ��C
 * �v���O�C���N���X�̃��[�h�Ɏ��s�����ꍇ�ɓ�������D
 * 
 * @author kou-tngt
 */
public class PluginClassLoadException extends PluginLoadException {

    /**
     * 
     */
    private static final long serialVersionUID = -996290505390023096L;

    public PluginClassLoadException() {
        super();
    }

    public PluginClassLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginClassLoadException(String message) {
        super(message);
    }

    public PluginClassLoadException(Throwable cause) {
        super(cause);
    }

}
