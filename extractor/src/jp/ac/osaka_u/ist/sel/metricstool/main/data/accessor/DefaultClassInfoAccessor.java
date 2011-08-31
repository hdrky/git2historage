package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


/**
 * �v���O�C���� ClassInfo �ɃA�N�Z�X���邽�߂ɗp����C���^�[�t�F�[�X
 * 
 * @author higo
 * 
 */
public class DefaultClassInfoAccessor implements ClassInfoAccessor {

    /**
     * ClassInfo �̃C�e���[�^��Ԃ��D ���̃C�e���[�^�͎Q�Ɛ�p�ł���ύX�������s�����Ƃ͂ł��Ȃ��D
     * 
     * @return ClassInfo �̃C�e���[�^
     */
    @Override
    public Iterator<TargetClassInfo> iterator() {
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final SortedSet<TargetClassInfo> classInfos = classInfoManager.getTargetClassInfos();
        return classInfos.iterator();
    }

    /**
     * �ΏۃN���X�̐���Ԃ����\�b�h.
     * 
     * @return �ΏۃN���X�̐�
     */
    @Override
    public int getClassCount() {
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        return classInfoManager.getTargetClassCount();
    }

}
