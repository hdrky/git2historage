package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


/**
 * 
 * ���̗�O�̓v���O�C���̃��[�h�����݁C���s�������ɓ�������D
 * 
 * @author kou-tngt
 */
public class PluginLoadException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 6849558492623155760L;

    public PluginLoadException() {
        super();
    }

    public PluginLoadException(String message) {
        super(message);
    }

    public PluginLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoadException(Throwable cause) {
        super(cause);
    }
}
