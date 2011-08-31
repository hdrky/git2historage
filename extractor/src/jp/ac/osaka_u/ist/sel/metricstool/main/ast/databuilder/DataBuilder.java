package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;

/**
 * �f�[�^�r���_�[�̃C���^�[�t�F�[�X�D
 * 
 * �r���_�̃A�N�e�B�u�C��A�N�e�B�u�̐؂�ւ�������C�ߋ��ɍ\�z�����f�[�^�̊Ǘ��C�擾�Ȃǂ��s�����\�b�h�Q����������D
 * 
 * @author kou-tngt, t-miyake
 * 
 * @param <T> �r���h�����f�[�^�̌^
 */
/**
 * @author t-miyake
 *
 * @param <T>
 */
public interface DataBuilder<T> extends AstVisitListener{
    
    /**
     * �r���_���A�N�e�B�u�ɂ���D
     */
    public void activate();
    
    /**
     * �ߋ��ɍ\�z�����f�[�^���N���A����D
     */
    public void clearBuiltData();
    
    /**
     * �r���_���A�N�e�B�u�ɂ���D
     */
    public void deactivate();
    
    /**
     * �ߋ��ɍ\�z���ꂽ�f�[�^�̃��X�g���擾�D
     * 
     * @return �ߋ��ɍ\�z���ꂽ�f�[�^�̃��X�g
     */
    public List<T> getBuiltDatas();
    
    /**
     * �ߋ��ɍ\�z���ꂽ�f�[�^�̐����擾�D
     * 
     * @return �ߋ��ɍ\�z���ꂽ�f�[�^�̐�
     */
    public int getBuiltDataCount();
    
    /**
     * �ߋ��ɍ\�z���ꂽ�f�[�^�̂����ł��Â��f�[�^���擾�D
     * 
     * @return �ߋ��ɍ\�z���ꂽ�f�[�^�̂����ł��Â��f�[�^
     */
      public T getFirstBuiltData();
    
    /**
     * �ߋ��ɍ\�z���ꂽ�f�[�^�̂����ł��V�����f�[�^���擾�D
     * 
     * @return �ߋ��ɍ\�z���ꂽ�f�[�^�̂����ł��V�����f�[�^
     */
    public T getLastBuildData();
    
    /**
     * �X�^�b�N���Ɏc���Ă���f�[�^�ŁC�ł��V�����\�z���ꂽ�f�[�^���X�^�b�N������o���ĕԂ�.
     * @return �X�^�b�N���Ɏc���Ă���f�[�^�ŁC�ł��V�����\�z���ꂽ�f�[�^�C�f�[�^���������null
     */
    public T popLastBuiltData();
    
    /**
     * �ߋ��ɍ\�z���ꂽ�f�[�^���P�ȏ㎝���Ă��邩�ǂ�����Ԃ�.
     * 
     * @return �ߋ��ɍ\�z���ꂽ�f�[�^���P�ȏ㑶�݂���ꍇ��true
     */
    public boolean hasBuiltData();
    
    /**
     * �r���_���A�N�e�B�u���ǂ����Ԃ��D
     * 
     * @return �r���_���A�N�e�B�u�̏ꍇ��true
     */
    public boolean isActive();
    
    /**
     * �r���_���������D
     * �ߋ��ɍ\�z���ꂽ�f�[�^�͑S�č폜�����D
     */
    public void reset();
}
