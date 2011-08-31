package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;


/**
 * �v���O�C���� FileInfo �ɃA�N�Z�X���邽�߂ɗp����C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public class DefaultFieldInfoAccessor implements FieldInfoAccessor {

    /**
     * FileInfo �̃C�e���[�^��Ԃ��D ���̃C�e���[�^�͎Q�Ɛ�p�ł���ύX�������s�����Ƃ͂ł��Ȃ��D
     * 
     * @return FileInfo �̃C�e���[�^
     */
    @Override
    public Iterator<TargetFieldInfo> iterator() {
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final SortedSet<TargetFieldInfo> fieldInfos = fieldInfoManager.getTargetFieldInfos();
        return fieldInfos.iterator();
    }

    /**
     * �Ώۃt�@�C���̐���Ԃ����\�b�h.
     * @return �Ώۃt�@�C���̐�
     */
    @Override
    public int getFieldCount() {
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        return fieldInfoManager.getTargetFieldCount();
    }

}
