import {Conversation, ConversationContent, ConversationScrollButton} from "@/components/ai-elements/conversation";
import {Message, MessageContent} from "@/components/ai-elements/message";

export default function Home() {
    return (
        <Conversation className="relative w-full" style={{ height: '500px' }}>
            <ConversationContent>
                <Message from={'user'}>
                    <MessageContent>Hi there!</MessageContent>
                </Message>
            </ConversationContent>
            <ConversationScrollButton />
        </Conversation>
    )
}
;
