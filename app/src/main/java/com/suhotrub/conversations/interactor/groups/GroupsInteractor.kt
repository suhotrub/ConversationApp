package com.suhotrub.conversations.interactor.groups

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupsInteractor @Inject constructor(
        val groupsRepository: GroupsRepository
) {

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

    fun inviteToGroup(groupGuid: String, userGuid:String) =
            groupsRepository.inviteToGroup(groupGuid,userGuid)

    fun getJoinedGroups(offset: Int) =
            groupsRepository.getJoinedGroups(offset)

    fun getCreatedGroups(offset: Int) =
            groupsRepository.getCreatedGroups(offset)

    fun getGroupParticipants(groupGuid: String, offset: Int) =
            groupsRepository.getGroupParticipants(groupGuid, offset)

}