package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.util.EventListener;


/**
 * �v���O�C���̎��s�I�����ɌĂяo����郊�X�i�C���^�t�F�[�X
 * @author kou-tngt
 *
 */
public interface ExecutionEndListener extends EventListener {
    /**
     * �v���O�C���̎��s�I�����ɌĂяo����郊�X�i
     * @param plugin
     */
    public void executionEnd(AbstractPlugin plugin);
}
