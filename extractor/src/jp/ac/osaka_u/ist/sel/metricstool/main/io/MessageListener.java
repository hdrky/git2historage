package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.EventListener;


/**
 * ���b�Z�[�W�C�x���g���󂯎�郊�X�i
 * @author kou-tngt
 *
 */
public interface MessageListener extends EventListener {

    /**
     * ���b�Z�[�W��M���\�b�h
     * @param event ���b�Z�[�W�C�x���g
     */
    public void messageReceived(MessageEvent event);
}
