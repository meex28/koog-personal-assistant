import {Conversation, ConversationContent, ConversationScrollButton} from "@/components/ai-elements/conversation";
import {Message, MessageContent} from "@/components/ai-elements/message";
import type {UIMessage} from "ai";

type MessageType = { from: UIMessage['role'], content: string };

interface ChatHistoryProps {
    messages: MessageType[];
}

export default function ChatHistory({ messages }: ChatHistoryProps) {
    return (
        <Conversation>
            <ConversationContent>
                {messages.map((message, index) => (
                    <Message from={message.from} key={index}>
                        <MessageContent>{message.content}</MessageContent>
                    </Message>
                ))}
            </ConversationContent>
            <ConversationScrollButton/>
        </Conversation>
    );
}