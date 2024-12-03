package kz.tutorial.jsonplaceholdertypicode.presentation.comments

import androidx.recyclerview.widget.DiffUtil
import kz.tutorial.jsonplaceholdertypicode.domain.entity.Comment


class CommentsDiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(p0: Comment, p1: Comment): Boolean {
        return p0.userId == p1.userId
    }

    override fun areContentsTheSame(p0: Comment, p1: Comment): Boolean {
        return p0 == p1 //Checks for equality of all of the fields inside comment data class
    }
}