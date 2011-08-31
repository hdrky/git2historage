package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Collections;
import java.util.List;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �f�[�^�r���_�[�̃A�_�v�g�����D
 * 
 * �r���_�̃A�N�e�B�u�C��A�N�e�B�u�̐؂�ւ�������C�ߋ��ɍ\�z�����f�[�^�̊Ǘ��C�擾�Ȃǂ��s�����\�b�h�Q����������D
 * 
 * @author kou-tngt, t-miyake
 * 
 * @param <T> �r���h�����f�[�^�̌^
 */
public abstract class DataBuilderAdapter<T> implements DataBuilder<T> {

    /**
     * �r���_���A�N�e�B�u�ɂ���D
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#activate()
     */
    public final void activate() {
        this.active = true;
    }

    /**
     * �ߋ��ɍ\�z�����f�[�^���X�^�b�N����폜����D
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#clearBuiltData()
     */
    public void clearBuiltData() {
        this.builtDataStack.clear();
    }

    /**
     * �r���_���A�N�e�B�u�ɂ���D
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#deactivate()
     */
    public final void deactivate() {
        this.active = false;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    public abstract void entered(final AstVisitEvent e);

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    public abstract void exited(final AstVisitEvent e) throws ASTParseException;

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#getBuiltDatas()
     */
    public final List<T> getBuiltDatas() {
        return Collections.unmodifiableList(this.builtDataStack);
    }

    /**
     * �X�^�b�N���Ɏc���Ă���f�[�^�̐���Ԃ��D
     * @return �\�z�����f�[�^�̐�
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#getBuiltDataCount()
     */
    public int getBuiltDataCount() {
        return this.builtDataStack.size();
    }

    /**
     * �X�^�b�N���Ɏc���Ă���f�[�^�ŁC�ł��Â��\�z���ꂽ�f�[�^��Ԃ��D
     * @return �X�^�b�N���Ɏc���Ă���f�[�^�ŁC�ł��Â��\�z���ꂽ�f�[�^�C�f�[�^���������null��Ԃ�
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#getFirstBuiltData()
     */
    public T getFirstBuiltData() {
        if (!this.builtDataStack.isEmpty()) {
            return this.builtDataStack.firstElement();
        } else {
            return null;
        }
    }

    /**
     * �X�^�b�N���Ɏc���Ă���f�[�^�ŁC�ł��V�����\�z���ꂽ�f�[�^��Ԃ��D
     * @return �X�^�b�N���Ɏc���Ă���f�[�^�ŁC�ł��V�����\�z���ꂽ�f�[�^�C�f�[�^���������null��Ԃ�
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#getLastBuildData()
     */
    public T getLastBuildData() {
        if (!this.builtDataStack.isEmpty()) {
            return this.builtDataStack.peek();
        } else {
            return null;
        }
    }

    /**
     * �X�^�b�N���Ɏc���Ă���f�[�^�ŁC�ł��V�����\�z���ꂽ�f�[�^���X�^�b�N������o���ĕԂ��D
     * @return �X�^�b�N���Ɏc���Ă���f�[�^�ŁC�ł��V�����\�z���ꂽ�f�[�^�C�f�[�^���������null��Ԃ�
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#popLastBuiltData()
     */
    public T popLastBuiltData() {
        if (!this.builtDataStack.isEmpty()) {
            return this.builtDataStack.pop();
        } else {
            return null;
        }
    }

    /**
     * �ߋ��ɍ\�z�����f�[�^����ȏ㎝���Ă��邩�ǂ�����Ԃ��D
     * @return �ߋ��ɍ\�z�����f�[�^����ȏ㎝���Ă���ꍇ��true
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#hasBuiltData()
     */
    public boolean hasBuiltData() {
        return !this.builtDataStack.isEmpty();
    }

    /**
     * �r���_�����݃A�N�e�B�u��Ԃł��邩�ǂ�����Ԃ��D
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#isActive()
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * �r���_������������D
     * �ߋ��ɍ\�z�����f�[�^�͍폜�����D
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#reset()
     */
    public void reset() {
        this.clearBuiltData();
    }

    /**
     * �������Ȃ��D
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener#visited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    public final void visited(final AstVisitEvent e) {
        // �g��Ȃ����Ƃɂ���
    }

    /**
     * �\�z�����f�[�^���X�^�b�N�ɓo�^����
     * @param data �o�^�������f�[�^
     */
    protected final void registBuiltData(final T data) {
        this.builtDataStack.add(data);
    }

    /**
     * ���̃r���_�����݃A�N�e�B�u�ł��邩�ǂ���
     */
    private boolean active = true;

    /**
     * �\�z�����f�[�^���Ǘ�����X�^�b�N
     */
    private final Stack<T> builtDataStack = new Stack<T>();
}
