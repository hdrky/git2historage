package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Member �C���^�[�t�F�[�X�𗘗p�����������s���N���X�D
 * 
 * @author higo
 * 
 */
public class StaticOrInstanceProcessing {

    /**
     * �����ŗ^����ꂽ�����o�[�̂����C�X�^�e�B�b�N�Ȃ��̂����𒊏o���ĕԂ��D
     * 
     * @param <T> �����o�[�̌^
     * @param members �����o�[�� List
     * @return �X�^�e�B�b�N�ȃ����o�[�� List
     */
    public static final <T extends StaticOrInstance> List<T> getInstanceMembers(final List<T> members) {

        final List<T> instanceMembers = new ArrayList<T>();
        for (T member : members) {
            if (member.isInstanceMember()) {
                instanceMembers.add(member);
            }
        }

        return Collections.unmodifiableList(instanceMembers);
    }

    /**
     * �����ŗ^����ꂽ�����o�[�̂����C�C���X�^���X�Ȃ��̂����𒊏o���ĕԂ��D
     * 
     * @param <T> �����o�[�̌^
     * @param members �����o�[�� List
     * @return �C���X�^���X�����o�[�� List
     */
    public static final <T extends StaticOrInstance> List<T> getStaticMembers(final List<T> members) {

        final List<T> staticMembers = new ArrayList<T>();
        for (T member : members) {
            if (member.isStaticMember()) {
                staticMembers.add(member);
            }
        }

        return Collections.unmodifiableList(staticMembers);
    }

    /**
     * �����ŗ^����ꂽ�����o�[�̂����C�X�^�e�B�b�N�Ȃ��̂����𒊏o���ĕԂ��D
     * 
     * @param <T> �����o�[�̌^
     * @param members �����o�[�� SortedSet
     * @return �X�^�e�B�b�N�ȃ����o�[�� SortedSet
     */
    public static final <T extends StaticOrInstance> SortedSet<T> getInstanceMembers(
            final SortedSet<T> members) {

        final SortedSet<T> instanceMembers = new TreeSet<T>();
        for (T member : members) {
            if (member.isInstanceMember()) {
                instanceMembers.add(member);
            }
        }

        return Collections.unmodifiableSortedSet(instanceMembers);
    }

    /**
     * �����ŗ^����ꂽ�����o�[�̂����C�C���X�^���X�Ȃ��̂����𒊏o���ĕԂ��D
     * 
     * @param <T> �����o�[�̌^
     * @param members �����o�[�� SortedSet
     * @return �C���X�^���X�����o�[�� SortedSet
     */
    public static final <T extends StaticOrInstance> SortedSet<T> getStaticMembers(final SortedSet<T> members) {

        final SortedSet<T> staticMembers = new TreeSet<T>();
        for (T member : members) {
            if (member.isStaticMember()) {
                staticMembers.add(member);
            }
        }

        return Collections.unmodifiableSortedSet(staticMembers);
    }
}
