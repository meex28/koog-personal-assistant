import {Conversation, ConversationContent, ConversationScrollButton} from "@/components/ai-elements/conversation";
import {Message, MessageContent} from "@/components/ai-elements/message";
import type {UIMessage} from "ai";
import {Response} from "@/components/ai-elements/response";

type MessageType = { from: UIMessage['role'], content: string };

interface ChatHistoryProps {
    messages: MessageType[];
}

export default function ChatHistory({ messages }: ChatHistoryProps) {
    return (
        <Conversation className="flex-1 overflow-y-auto">
            <ConversationContent>
                {messages.map((message, index) => (
                    <Message from={message.from} key={index}>
                        <MessageContent>
                            <Response>
                                {message.content}
                            </Response>
                        </MessageContent>
                    </Message>
                ))}
            </ConversationContent>
            <ConversationScrollButton/>
        </Conversation>
    );
}