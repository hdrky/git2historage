package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * �v���O�C���� MethodInfo �ɃA�N�Z�X���邽�߂ɗp����C���^�[�t�F�[�X
 * 
 * @author higo
 * 
 */
public class DefaultMethodInfoAccessor implements MethodInfoAccessor {

    /**
     * MethodInfo �̃C�e���[�^��Ԃ��D ���̃C�e���[�^�͎Q�Ɛ�p�ł���ύX�������s�����Ƃ͂ł��Ȃ��D
     * 
     * @return MethodInfo �̃C�e���[�^
     */
    @Override
    public Iterator<TargetMethodInfo> iterator() {
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();
        final SortedSet<TargetMethodInfo> methodInfos = methodInfoManager.getTargetMethodInfos();
        return methodInfos.iterator();
    }

    /**
     * �Ώۃ��\�b�h�̐���Ԃ����\�b�h.
     * 
     * @return �Ώۃ��\�b�h�̐�
     */
    @Override
    public int getMethodCount() {
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();
        return methodInfoManager.getTargetMethodCount();
    }

}
