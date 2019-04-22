package com.suhotrub.conversations.interactor.groups

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс для работы с группами
 * @param groupsRepository репозиторий групп
 */
@Singleton
class GroupsInteractor @Inject constructor(
        val groupsRepository: GroupsRepository
) {

    /**
     * Создание группы
     * @param name название группы
     * @param description группы
     *
     */
    fun createGroup(
            name: String,
            description: String?,
            avatarLink: String,
            users:List<String>
    ) = groupsRepository.createGroup(
            name,
            description,
            avatarLink,
            users
    )


    /**
     * Приглашение в группу
     *
     */
    fun inviteToGroup(groupGuid: String, userGuid:String) =
            groupsRepository.inviteToGroup(groupGuid,userGuid)

    fun leaveGroup(groupGuid: String) =
            groupsRepository.leaveGroup(groupGuid)

    fun getJoinedGroups(offset: Int) =
            groupsRepository.getJoinedGroups(offset)


    fun getGroupParticipants(groupGuid: String, offset: Int) =
            groupsRepository.getGroupParticipants(groupGuid, offset)

}